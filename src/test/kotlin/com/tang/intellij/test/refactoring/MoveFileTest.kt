

package com.tang.intellij.test.refactoring

//import com.intellij.openapi.fileEditor.FileDocumentManager
//import com.intellij.psi.PsiElement
//import com.intellij.refactoring.MultiFileTestCase
//import com.intellij.refactoring.move.moveFilesOrDirectories.MoveFilesOrDirectoriesProcessor
//
//class MoveFileTest : MultiFileTestCase() {
//    override fun getTestRoot() = "/refactoring/"
//
//    override fun getTestDataPath() = "src/test/resources/"
//
//    fun testMoveFile() {
//        val fileToMove = "A.lua"
//        val targetDirName = "to"
//        doTest { rootDir, _ ->
//            val child = rootDir.findFileByRelativePath(fileToMove)
//            assertNotNull("File $fileToMove not found", child)
//            val file = myPsiManager.findFile(child!!)!!
//
//            val child1 = rootDir.findChild(targetDirName)
//            assertNotNull("File $targetDirName not found", child1)
//            val targetDirectory = myPsiManager.findDirectory(child1!!)
//
//            MoveFilesOrDirectoriesProcessor(myProject, arrayOf<PsiElement>(file), targetDirectory!!,
//                    false, false, null, null).run()
//
//            FileDocumentManager.getInstance().saveAllDocuments()
//        }
//    }
//}