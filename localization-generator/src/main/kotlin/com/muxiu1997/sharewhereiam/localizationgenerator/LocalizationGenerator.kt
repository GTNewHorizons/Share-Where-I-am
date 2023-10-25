package com.muxiu1997.sharewhereiam.localizationgenerator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ClassName

import kotlin.concurrent.thread

object LocalizationGenerator {
    private val properties = mutableListOf<LocalizationData>()

    fun generate(codeGenerator: CodeGenerator) {
        thread(true) {
            val fileSpec = FileSpec.builder("com.muxiu1997.sharewhereiam.loader", "LocalizationLoader")
                .addType(
                    TypeSpec.objectBuilder("LocalizationLoader")
                        .addFunction(
                            FunSpec.builder("load").also { funBuilder ->
                                properties.forEach { data ->
                                    propertyToStatement(funBuilder, data)
                                }
                            }
                                .build()
                        ).build()
                ).build()
            codeGenerator.createNewFile(
                dependencies = Dependencies(true),
                packageName = fileSpec.packageName,
                fileName = fileSpec.name,
            ).use { outputStream ->
                outputStream.writer().use {
                    fileSpec.writeTo(it)
                }
            }
            val langFile = StringBuilder()
            properties.forEach { langFile.append("${it.key}=${it.en}\n") }
            codeGenerator.createNewFile(
                dependencies = Dependencies(true),
                packageName = "assets.sharewhereiam.lang",
                fileName = "en_US",
                extensionName = "lang"
            ).use { outputStream ->
                outputStream.writer().use {
                    it.write(langFile.toString())
                }
            }
        }
    }

    fun add(property: KSPropertyDeclaration, annotationType: KSType) {
        val qualifiedName = property.qualifiedName!!
        val anno = property.annotations.find { it.annotationType.resolve() == annotationType }!!
        val key =
            "${anno.arguments.find { it.name?.getShortName() == "prefix" }!!.value!!}.${qualifiedName.getShortName()}"
        val en = anno.arguments.find { it.name?.getShortName() == "en" }!!.value!!.toString()
        val data = LocalizationData(
            cls = qualifiedName.getQualifier(),
            name = qualifiedName.getShortName(),
            key = key,
            en = en
        )
        properties.add(data)
    }

    private fun propertyToStatement(funBuilder: FunSpec.Builder, data: LocalizationData) {
        val p = MemberName(data.cls, data.name)
        val localizedString = ClassName(data.cls, "LocalizedString")
        funBuilder.addStatement("%M = %T(%S)", p, localizedString, data.key)
    }
}
