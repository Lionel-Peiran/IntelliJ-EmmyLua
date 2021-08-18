--[[

Copyright (c) 2014-2017 Chukong Technologies Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

]]
swallower = setmetatable({},{
	__add = function() return 0 end,
	__sub = function() return 0 end,
	__mul = function() return 0 end,
	__div = function() return 0 end,
	__mod = function() return 0 end,
	__pow = function() return 0 end,
	__unm = function() return 0 end,
	__idiv = function() return 0 end,
	__band = function() return 0 end,
	__bor = function() return 0 end,
	__bxor = function() return 0 end,
	__bnot = function() return 0 end,
	__shl = function() return 0 end,
	__shr = function() return 0 end,
	__concat = function() return "" end,
	__len = function() return 0 end,
	__eq = function() return false end,
	__lt = function() return false end,
	__le = function() return false end,
	__index = function(self) return self end,
	__newindex = function() end,
	__call = function(self) return self end,
})

---@return string
---@param v any
local function dump_value_(v)
	if type(v) == "string" then
		v = "\"" .. v .. "\""
	end
	return tostring(v)
end

function dump(value, description, nesting)
	if type(nesting) ~= "number" then nesting = 3 end

	local lookupTable = {}
	local result = {"", }

	-- local traceback = string.split(debug.traceback("", 2), "\n")
	-- print("dump from: " .. string.trim(traceback[3]))

	local function dump_(value, description, indent, nest, keylen)
		description = description or "<var>"
		local spc = ""
		if type(keylen) == "number" then
			spc = string.rep(" ", keylen - string.len(dump_value_(description)))
		end
		if type(value) ~= "table" and type(value) ~= "dict" then
			result[#result +1 ] = string.format("%s%s%s = %s", indent, dump_value_(description), spc, dump_value_(value))
		elseif lookupTable[value] then
			result[#result +1 ] = string.format("%s%s%s = *REF*", indent, dump_value_(description), spc)
		else
			lookupTable[value] = true
			if nest > nesting then
				result[#result +1 ] = string.format("%s%s = *MAX NESTING*", indent, dump_value_(description))
			else
				result[#result +1 ] = string.format("%s%s = {", indent, dump_value_(description))
				local indent2 = indent.."	"
				local keys = {}
				local keylen = 0
				local values = {}
				for k, v in pairs(value) do
					keys[#keys + 1] = k
					local vk = dump_value_(k)
					local vkl = string.len(vk)
					if vkl > keylen then keylen = vkl end
					values[k] = v
				end
				table.sort(keys, function(a, b)
					if type(a) == "number" and type(b) == "number" then
						return a < b
					else
						return tostring(a) < tostring(b)
					end
				end)
				for i, k in ipairs(keys) do
					dump_(values[k], k, indent2, nest + 1, keylen)
				end
				result[#result +1] = string.format("%s}", indent)
			end
		end
	end
	dump_(value, description, "- ", 1)
	-- for i, line in ipairs(result) do
	-- 	print(line)
	-- end
	return table.concat(result, "\n")
end

function float(value, base)
	return tonumber(value, base) or 0
end

function int(value, base)
	return math.floor(tonumber(value, base) or 0)
end

str = tostring

function b2i(b)
	if b then return 1 else return 0 end
end

function callable(x)
	if type(x) == "function" then
		return true
	elseif type(x) == "table" then
		local mt = getmetatable(x)
		return type(mt) == "table" and type(mt.__call) == "function"
	end
	return false
end

function os.excepthook(err)
	if _G.__G__TRACKBACK__ then
		_G.__G__TRACKBACK__(err)
	else
		print(debug.traceback(err))
	end
end

local succ, hexlib = pcall(require, "hexlib")
if succ and hexlib ~= nil then
	try_catch_call = hexlib.try_catch_call
else
	function try_catch_call(func, ...)
		return xpcall(func, os.excepthook, ...)
	end
end

__decorators = list()

function decorator_wrapper(f)
	__decorators:append(f)
end

decorator_metatable = {
	__newindex = function(t, k, v)
		if type(v) == "function" then
			-- decorate function
			for i = #__decorators, 1, -1 do
				-- multiple decorators
				v = __decorators[i](t, k, v)
				__decorators:pop()
			end
		elseif isclass(v) then
			-- decorate class
			for i = #__decorators, 1, -1 do
				v = __decorators[i](t, k, v)
				__decorators:pop()
			end
		end
		rawset(t, k, v)
	end,
	__metatable = "__module__",
}

function property()
	decorator_wrapper(function(cls, fname, func)
		if not rawget(cls, "__getters") then
			if cls.super and cls.super.__getters then
				cls.__getters = cls.super.__getters:copy()
			else
				cls.__getters = dict()
			end
		end
		cls.__getters[fname] = func
		return nil
	end)
end

function property_setter()
	decorator_wrapper(function(cls, fname, func)
		if not cls.__setters then
			if cls.super and cls.super.__setters then
				cls.__setters = cls.super.__setters:copy()
			else
				cls.__setters = dict()
			end
		end
		cls.__setters[fname] = func
		return nil
	end)
end

if succ and hexlib ~= nil then

	cache_metatable = hexlib.cache_metatable
	class_end = hexlib.class_end
	class = hexlib.class

else

	function cache_metatable(cls)
		local mt = {}
		for _, method in pairs(list("__add", "__band", "__bnot", "__bxor", "__close", "__concat", "__div",
			"__eq", "__gc", "__idiv", "__le", "__len", "__lt", "__mod", "__mode", "__mul", "__name",
			"__pairs", "__pow", "__shl", "__shr", "__sub", "__tostring", "__unm")) do
			if cls[method] then
				mt[method] = cls[method]
			end
		end
		if not cls.__tostring then
			mt.__tostring = function(self)
				return string.format("<class %s object at 0x%x>", cls.__name__, id(self))
			end
		end
		if cls.__index or cls.__getters then
			mt.__index = function(self, k)
				local v = cls[k]
				if v then
					return v
				end
				local getters = cls.__getters
				if getters and getters:contains(k) then
					return getters[k](self)
				end
				if cls.__index then
					return cls.__index(self, k)
				end
			end
		else
			mt.__index = cls
		end
		if cls.__newindex or cls.__setters then
			mt.__newindex = function(self, k, v)
				if cls.__setters and cls.__setters:contains(k) then
					cls.__setters[k](self, v)
				elseif cls.__newindex then
					cls.__newindex(self, k, v)
				else
					rawset(self, k, v)
				end
			end
		end
		if cls.__call__ then
			mt.__call = cls.__call__
		end
		rawset(cls, "__cache_metatable__", mt)
	end

	function class_end(cls)
		if rawget(cls, "__cache_metatable__") then
			return
		end
		if cls.super and not rawget(cls.super, ".isclass") then
			class_end(cls.super)
		end
		if cls.__metaclass__ then
			cls:__metaclass__()
		end
		cache_metatable(cls)
		return cls
	end

	function class(classname, super, is_singleton)
		local superType = type(super)
		assert(
			superType == "nil" or superType == "table",
			("class() - create class \"%s\" with invalid super class type \"%s\""):format(classname, superType)
		)

		if super then is_singleton = is_singleton or super.__singleton__ end

		local cls = {__name__ = classname, __singleton__ = is_singleton, __is_class__ = true}

		cls.super = super
		-- add default new
		if not super or not super.new then
			cls.new = function() return {} end
		end
		-- add default constructor
		if not super or not super.ctor then
			cls.ctor = function() end
		end
		if super then
			if rawget(super, ".isclass") then
				-- super is native class
				cls.__engine_class__ = super
			else
				-- else super is pure lua class
				cls.__engine_class__ = super.__engine_class__
			end
		end

		cls.create = function(cls, ...)
			if rawget(cls, "__cache_metatable__") == nil then
				class_end(cls)
			end
			if cls.__singleton__ and rawget(cls, "__instance") then
				return cls.__instance
			end
			local instance = cls:new(...)
			if not instance then
				return nil
			end
			if instance.__constructed then
				-- 支持单例
				return instance
			end
			instance.__class__ = cls
			if not getmetatable(instance) then
				setmetatable(instance, cls.__cache_metatable__)
			end
			instance:ctor(...)
			instance.__constructed = true
			if cls.__singleton__ then
				cls.__instance = instance
			end
			return instance
		end

		setmetatable(cls, {
			__index = super,
			__call = function(cls, ...)
				return cls:create(...)
			end,
			__newindex = function(cls, k, v)
				if type(v) == "function" then
					for i = #__decorators, 1, -1 do
						v = __decorators[i](cls, k, v)
						__decorators:pop()
					end
				end
				rawset(cls, k, v)
			end,
			__tostring=function(self)
				return string.format("<class '%s'> at 0x%x", self.__name__, id(self))
			end
		})

		return cls
	end
end

local EXCLUDE_ATTR = {
	__is_class__ = true,
	__name__ = true,
	__engine_class__ = true,
	__cache_metatable__ = true,
	__singleton__ = true,
	__instance = true,
	__metaclass__ = true,
	super = true,
	new = true,
	create = true,
	ctor = true,
}

function dirclass(cls, filter)
	local dirs = {}
	for k, v in pairs(cls) do
		if not EXCLUDE_ATTR[k] then
			if filter then
				if filter == type(v) then
					dirs[k] = v
				end
			else
				dirs[k] = v
			end
		end
	end
	if cls.super then
		for k, v in dirclass(cls.super) do
			if not dirs[k] and not EXCLUDE_ATTR[k] then
				if filter then
					if filter == type(v) then
						dirs[k] = v
					end
				else
					dirs[k] = v
				end
			end
		end
	end
	return next, dirs
end

function isclass(cls)
	if type(cls) == "table" and (rawget(cls, "__is_class__") or rawget(cls, ".isclass")) then
		return true
	end
	return false
end

function issubclass(cls, base)
	if not isclass(cls) then
		return false
	end
	if cls == base then
		return true
	elseif cls.super then
		return issubclass(cls.super, base)
	end
	return false
end

function len(obj)
	return #obj
end

function hasattr(obj, name)
	if (type(obj) == "table" or type(obj) == "userdata") then
		local attr
		if not pcall(function() attr = obj[name] end) then
			return false
		end
		if attr == nil then
			return false
		end
		return true
	end
	return false
end

function getattr(obj, name, default)
	if hasattr(obj, name) then
		return obj[name]
	else
		return default
	end
end

function isinstance(obj, cls)
	if subtype(obj) == rawget(cls, ".subtype") then
		return true
	end
	local ret = false
	if hasattr(obj, "__class__") then
		ret = issubclass(obj.__class__, cls)
	end
	if not ret and tolua then
		if "class " .. tolua.type(obj) == tolua.type(cls) then
			ret = true
		end
	end
	return ret
end


function inheritclass(child_clz, super_clz)
	for k, v in dirclass(super_clz) do
		if not child_clz[k] then
			child_clz[k] = v
		end
	end
end

local _iter_list_meta = {
	next=function(self)
		if self._i > #self._o then
			return nil
		end
		self._i = self._i + 1
		return self._o[self._i - 1]
	end,
	__pairs=function(self)
		return self.next, self, nil
	end
}
_iter_list_meta.__index = _iter_list_meta

local _iter_meta = {
	next=function(self)
		local k, v = next(self._o, self._i)
		self._i = k
		return k, v
	end,
	__pairs=function(self)
		return self.next, self, nil
	end
}
_iter_meta.__index = _iter_meta

function iter(iterable, is_list)
	if is_list == nil or is_list then
		return setmetatable({_o=iterable, _i=1}, _iter_list_meta)
	else
		return setmetatable({_o=iterable}, _iter_meta)
	end
end

function zip(...)
	local iterators = table.map({...}, function(e) if type(e) == "list" or not e.next then return iter(e) else return e end end)
	local res = {}
	while true do
		local e = {}
		for _, it in ipairs(iterators) do
			local v = it:next()
			if v then
				table.insert(e, v)
			else
				return res
			end
		end
		table.insert(res, e)
	end
	return res
end

function math.newrandomseed()
	math.randomseed(math.floor(tonumber(tostring((os.ts() - math.floor(os.ts())) * 1000000):reverse())))
end

function math.round(value, n)
	local move_factor = n and 10^n or 1
	value = float(value)
	return math.floor(value * move_factor + 0.5)/move_factor
end

local pi_div_180 = math.pi / 180
function math.angle2radian(angle)
	return angle * pi_div_180
end

function math.radian2angle(radian)
	return radian * 180 / math.pi
end

function math.uniform(a, b)
	return a + (b - a) * math.random()
end

local function randbelow_without_getrandbits(n, maxsize)
	maxsize = maxsize and maxsize or 1<< 53
	if n >= maxsize then
		return math.floor(math.random() * n)
	end
	if n == 0 then
		return 0
	end
	local rem = maxsize % n
	local limit = (maxsize - rem) / maxsize
	local r = math.random()
	while (r >= limit) do
		r = math.random()
	end
	return math.floor(r * maxsize) % n
end

function math.sample(population, k)
	-- 参照python的random.sample实现
	local n = #population
	assert(n >= k, ("arr size %d greater than k %d"):format(n, k))
	local result = list()
	local setsize = 21 -- size of a small set minus size of an empty list
	if k > 5 then
		setsize = setsize + 4 ^ (math.ceil(math.log(k * 3, 4)))
	end
	if n <= setsize then
		local pool = population:copy()
		for i=1, k do
			local j = randbelow_without_getrandbits(n - i) + 1
			result:append(pool[j])
			pool[j] = pool[n - i + 1] -- move non-selected item into vacancy
		end
	else
		local selected = {}
		for i=1, k do
			local j = randbelow_without_getrandbits(n) + 1
			while (selected[j]) do
				j = randbelow_without_getrandbits(n) + 1
			end
			selected[j] = true
			result:append(population[j])
		end
	end
	return result
end

function math.choice(array)
	return array[math.random(#array)]
end

function math.sum(array)
	local s = 0
	for _, v in pairs(array) do
		s = s + v
	end
	return s
end

function math.shuffle(array)
	for i = #array, 2, -1 do
		local j = math.max(1, math.floor(math.random() * (i + 1)))
		array[i], array[j] = array[j], array[i]
	end
end

function math.add(x, y)
	return x + y
end

function math.sub(x, y)
	return x - y
end

function math.mul(x, y)
	return x * y
end

function math.div(x, y)
	return x / y
end

function io.exists(path)
	local file = io.open(path, "r")
	if file then
		io.close(file)
		return true
	end
	return false
end

function io.readfile(path)
	local file = io.open(path, "rb")
	if file then
		file:seek("set")
		local content = file:read("a")
		io.close(file)
		return content
	end
	return nil
end

function io.writefile(path, content, mode)
	mode = mode or "w+b"
	local file = io.open(path, mode)
	if file then
		if file:write(content) == nil then return false end
		io.close(file)
		return true
	else
		return false
	end
end

function io.pathinfo(path)
	local pos = string.len(path)
	local extpos = pos + 1
	while pos > 0 do
		local b = string.byte(path, pos)
		if b == 46 then -- 46 = char "."
			extpos = pos
		elseif b == 47 then -- 47 = char "/"
			break
		end
		pos = pos - 1
	end

	local dirname = string.sub(path, 1, pos)
	local filename = string.sub(path, pos + 1)
	extpos = extpos - pos
	local basename = string.sub(filename, 1, extpos - 1)
	local extname = string.sub(filename, extpos)
	return {
		dirname = dirname,
		filename = filename,
		basename = basename,
		extname = extname
	}
end

function io.filesize(path)
	local size = false
	local file = io.open(path, "r")
	if file then
		local current = file:seek()
		size = file:seek("end")
		file:seek("set", current)
		io.close(file)
	end
	return size
end

local function isarray(t)
	if type(t) ~= "table" then
		return false
	end
	local n = #t
	local real_n = 0
	for i, v in pairs(t) do
		if type(i) ~= "number" then
			return false
		end
		if i < 1 or i > n then
			return false
		end
		real_n = real_n + 1
		if real_n > n then
			return false
		end
	end
	return true
end

function table.str(t)
	local is_array = isarray(t)
	local items = {}
	local keys = table.keys(t)
	table.sort(keys)
	for _, k in ipairs(keys) do
		local v = t[k]
		if type(v) == "table" then
			if is_array then
				table.insert(items, table.str(v))
			else
				table.insert(items, tostring(k)..":"..table.str(v))
			end
		else
			if is_array then
				table.insert(items, tostring(v))
			else
				table.insert(items, tostring(k)..":"..tostring(v))
			end
		end
	end
	return "{"..table.concat(items, ",").."}"
end

function table.copy(t)
	local new = {}
	for k, v in pairs(t) do
		new[k] = v
	end
	return new
end

function table.deepcopy(t)
	local new = dict()
	if type(t) == "dict" then
	elseif type(t) == "list" then
		new = list()
	elseif type(t) == "table" then
		new = {}
	end
	if type(t) == "table" or type(t) == "dict" then
		for k, v in pairs(t) do
			if type(v) == "table" or type(v) == "list" or type(v) == "dict" then
				new[k] = table.deepcopy(v)
			else
				new[k] = v
			end
		end
	elseif type(t) == "list" then
		for _, v in pairs(t) do
			if type(v) == "table" or type(v) == "list" or type(v) == "dict" then
				new:append(table.deepcopy(v))
			else
				new:append(v)
			end
		end
	end
	return new
end

function table.nums(t)
	local count = 0
	for k, v in pairs(t) do
		count = count + 1
	end
	return count
end

function table.keys(hashtable, filter_func)
	local keys = {}
	for k, v in pairs(hashtable) do
		if not filter_func or filter_func(k) then
			table.insert(keys, k)
		end
	end
	return keys
end

function table.values(hashtable, filter_func)
	local values = {}
	for k, v in pairs(hashtable) do
		if not filter_func or filter_func(v) then
			table.insert(values, v)
		end
	end
	return values
end

function table.setdefault(hashtable, key, default)
	if not hashtable[key] then
		hashtable[key] = default
	end
	return hashtable[key]
end

function table.pop(table, key, default)
	local value = table[key]
	if value then
		table[key] = nil
		return value
	end
	return default
end

function table.merge(dest, src, replace)
	if replace == nil then replace = true end
	if replace then
		for k, v in pairs(src) do
			dest[k] = v
		end
	else
		for k, v in pairs(src) do
			dest[k] = dest[k] or v
		end
	end
	return dest
end

function table.indexof(array, value, begin, fn)
	for i = begin or 1, #array do
		if fn then
			if fn(array[i], value) then return i end
		else
			if array[i] == value then return i end
		end
	end
	return 0
end

function table.extend(array, items)
	for _, item in ipairs(items) do
		table.insert(array, item)
	end
end

function table.concat_array(array_a, array_b)
	local c = table.copy(array_a)
	table.extend(c, array_b)
	return c
end

function table.slice(array, first, last)
	first = first or 1
	if first > #array then
		return {}
	end
	last = last or (#array + 1)
	last = math.min(last, #array + 1)
	local ret = {}
	for i = first, last - 1 do
		ret[i - first + 1] = array[i]
	end
	return ret
end

function table.keyof(hashtable, value)
	for k, v in pairs(hashtable) do
		if v == value then return k end
	end
	return nil
end

function table.map(t, fn)
	for k, v in pairs(t) do
		t[k] = fn(v, k)
	end
	return t
end

function table.filter(t, fn)
	for k, v in pairs(t) do
		if not fn(v, k) then t[k] = nil end
	end
	return t
end

function table.reduce(array, fn)
	local acc = array[1]
	for i = 2, #array do
		acc = fn(acc, array[i])
	end
	return acc
end

function table.any(t, fn)
	if fn then
		for k, v in pairs(t) do
			if fn(v, k) then
				return true
			end
		end
	else
		for k, v in pairs(t) do if v then return true end end
	end
	return false
end

function table.all(t, fn)
	if fn then
		for k, v in pairs(t) do
			if not fn(v, k) then
				return false
			end
		end
	else
		for k, v in pairs(t) do if not v then return false end end
	end
	return true
end

function table.urlencode(query)
	if type(query) ~= "dict" then
		error("not a valid url query")
	end
	local tmp = list()
	for k, v in pairs(query) do
		tmp:append(tostring(k):urlencode().."="..tostring(v):urlencode())
	end
	return table.concat(tmp, "&")
end

function table.intersection(t1, ...)
	local res = {}
	for k, v in pairs(t1) do
		for _, t in ipairs({...}) do
			if t[k] == nil then
				goto continue
			end
		end
		table.insert(res, k)
	::continue::
	end
	return res
end

function table.reverse(t)
	local n = #t
	local i = 1
	while i < n do
		t[i],t[n] = t[n],t[i]
		i = i + 1
		n = n - 1
	end
end

function table.is_list(t)
	local n = #t
	for k, v in pairs(t) do
		if type(k) ~= "number" then
			return false
		end
		if k > n then
			return false
		end
	end
	return true
end

function table.to_dict(t)
	local d = dict()
	for k, v in pairs(t) do
		if type(v) == "table" then
			if table.is_list(v) then
				d[k] = table.to_list(v)
			else
				d[k] = table.to_dict(v)
			end
		else
			d[k] = v
		end
	end
	return d
end

function table.lk_to_dict(t)
	-- coding时临时用下，规避下语法错误
	return table.to_dict(t)
end

function table.to_list(t)
	local l = list()
	for i, v in pairs(t) do
		if type(v) == "table" then
			if table.is_list(v) then
				l:append(table.to_list(v))
			else
				l:append(table.to_dict(v))
			end
		else
			l:append(v)
		end
	end
	return l
end

function table.to_dict_or_list(t)
	local ret
	if table.is_list(t) then
		ret = table.to_list(t)
	else
		ret = table.to_dict(t)
	end
	return ret
end

function string.split(input, delimiter, num)
	if not delimiter or #delimiter == 0 then
		input = string.strip(input)
		input = input:gsub("%s+", " ")
		delimiter = " "
	end
	local pos, arr, cnt = 0, list(), 0
	num = num or #input
	-- for each divider found
	for st, sp in function() return input:find(delimiter, pos, true) end do
		arr:append(input:sub(pos, st - 1))
		pos = sp + 1
		cnt = cnt + 1
		if cnt >= num then
			break
		end
	end
	arr:append(input:sub(pos))
	return arr
end

function string.strip(str, del)
	str = tostring(str)
	if (del) then
		del = tostring(del)
	else
		del = tostring(" ")
	end
	local i, j = 1, string.len(str)
	local stringB = 1, stringE
	while i <= string.len(str) do
		if string.sub(str, i, i) ~= del then
			stringB = i
			break
		else
			i = i + 1
		end
	end
	if i > j then
		return ""
	end
	while j > 0 do
		if string.sub(str, j, j) ~= del then
			stringE = j
			break
		else
			j = j - 1
		end
	end
	return string.sub(str, stringB, stringE)
end

function string.lstrip(str, del)
	str = tostring(str)
	if (del) then
		del = tostring(del)
	else
		del = tostring(" ")
	end
	local i, j = 1, string.len(str)
	local stringB = 1
	while i <= string.len(str) do
		if string.sub(str, i, i) ~= del then
			stringB = i
			break
		else
			i = i + 1
		end
	end
	if i > j then
		return ""
	end
	return string.sub(str, stringB, string.len(str))
end

function string.is_digit(str)
	if str == nil then
		return nil, "the string parameter is nil"
	end
	local len = string.len(str)
	for i = 1, len do
		local ch = string.sub(str, i, i)
		if ch < '0' or ch > '9' then
			return false
		end
	end
	return true
end

function string.pformat(sstr, args, kwargs)
	-- 处理python形式的format,3种形式, 并支持数字格式化({:.2f}, {:.2%}, {:02d}, {:.2e})：
	-- 1、   "{}{}".format(1, 2) "{:.1f}{:.3f}".format(1, 2) 传入args的list
	-- 2、   "{0}{1}".format(1, 2) "{0:.0f}{1:.0f}".format(1, 2) 传入args的list
	-- 3、   "{a}{b}".format(a=1, b=2) "{a:.2f}{b:.3e}".format(a=1, b=2) 传入kwargs的dict
	args = args or list()
	kwargs = kwargs or dict()
	if not bool(args) and not bool(kwargs) then
		return sstr
	end 
	local dstr = sstr
	local idx_val = 1
	for str in string.gmatch(dstr, "%(-{.-}%)-") do
		local tstr = string.gsub(str, "%(-{", "")
		tstr = string.gsub(tstr, "}%)-", "")
		if bool(tstr) then
			local idx_colon = string.find(tstr, ":")
			local key = idx_colon and string.sub(tstr, 1, idx_colon-1) or tstr
			local fmt = idx_colon and string.sub(tstr, idx_colon+1) or "s"
			local value
			local sub_str = idx_colon and "{"..key..":"..fmt.."}" or "{"..key.."}"
			if int(key) and int(key) < #args and int(key) >= 0 then
				value = args[int(key)+1]
			elseif kwargs and kwargs:contains(key) then
				value = kwargs[key]
			elseif not int(key) then
				value = args[idx_val]
				idx_val = idx_val + 1
			end
			-- 支持下python中:.2%这种形式的数字格式化
			local idx_per = string.find(fmt, "%%")
			if idx_per and type(value) == "number" then
				sub_str = "{"..key..":"..string.gsub(fmt, "%%", "%%%%").."}"
				fmt = string.gsub(fmt, "%%", "f")
				dstr = string.gsub(dstr, sub_str, string.format("%"..fmt, 100 * value))
			elseif not idx_per then
				dstr = string.gsub(dstr, sub_str, string.format("%"..fmt, value))
			end
		end
	end
	dstr = string.gsub(dstr, "%(-{}%)-", "%%s")
	dstr = string.gsub(dstr, "%(-{", "")
	dstr = string.gsub(dstr, "}%)-", "")
	dstr = string.format(dstr, args:unpack())
	return dstr
end

local now = os.time()
os.timezone = math.floor(os.difftime(now, os.time(os.date("!*t", now))))
