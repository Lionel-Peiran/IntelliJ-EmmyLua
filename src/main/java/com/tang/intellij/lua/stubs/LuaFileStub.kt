

package com.tang.intellij.lua.stubs

import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiFile
import com.intellij.psi.StubBuilder
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import com.intellij.util.io.StringRef
import com.tang.intellij.lua.lang.LuaLanguage
import com.tang.intellij.lua.lang.LuaParserDefinition
import com.tang.intellij.lua.psi.LuaPsiFile

/**

 * Created by tangzx on 2016/11/27.
 */
class LuaFileElementType : IStubFileElementType<LuaFileStub>(LuaLanguage.INSTANCE) {

    companion object {
        val LOG = Logger.getInstance(LuaFileElementType::class.java)
    }

    // debug performance
    override fun parseContents(chameleon: ASTNode): ASTNode? {
        val psi = chameleon.psi
        val t = System.currentTimeMillis()
        val contents = super.parseContents(chameleon)
        if (psi is LuaPsiFile) {
            if (LOG.isDebugEnabled) {
                val dt = System.currentTimeMillis() - t
                val fileName = psi.name
                println("$fileName : $dt")
                LOG.debug("$fileName : $dt")
            }
        }
        return contents
    }

    override fun getBuilder(): StubBuilder {
        return object : DefaultStubBuilder() {

            private var isTooLarger = false

            override fun createStubForFile(file: PsiFile): StubElement<*> {
                if (file is LuaPsiFile){
                    isTooLarger = file.tooLarger
                    return LuaFileStub(file)
                }
                return super.createStubForFile(file)
            }

            override fun skipChildProcessingWhenBuildingStubs(parent: ASTNode, node: ASTNode): Boolean {
                return isTooLarger
            }
        }
    }

    override fun serialize(stub: LuaFileStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.module)
        dataStream.writeUTFFast(stub.uid)
        if (LOG.isTraceEnabled) {
            println("--------- START: ${stub.psi.name}")
            println(stub.printTree())
            println("--------- END: ${stub.psi.name}")
        }
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): LuaFileStub {
        val moduleRef = dataStream.readName()
        val uid = dataStream.readUTFFast()
        return LuaFileStub(null, StringRef.toString(moduleRef), uid)
    }

    override fun getExternalId() = "lua.file"
}

class LuaFileStub : PsiFileStubImpl<LuaPsiFile> {
    private var file: LuaPsiFile? = null
    private var moduleName:String? = null

    val uid: String

    constructor(file: LuaPsiFile) : this(file, file.moduleName, file.uid)

    constructor(file: LuaPsiFile?, module:String?, uid: String) : super(file) {
        this.file = file
        this.uid = uid
        moduleName = module
    }

    val module: String? get() {
        return moduleName
    }

    override fun getType(): LuaFileElementType = LuaParserDefinition.FILE
}