package com.yiduihuan;

import org.beetl.ext.nutz.BeetlViewMaker;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

@IocBy(type = ComboIocProvider.class, args = { "*org.nutz.ioc.loader.json.JsonLoader", "ioc/",
		"*org.nutz.ioc.loader.annotation.AnnotationIocLoader", "com.yiduihuan" })
@Encoding(input = "utf8", output = "utf8")
@Modules(scanPackage = true)
@Localization("msg")
@Ok("ioc:json")
@Fail("json")
@Views({ BeetlViewMaker.class })
public class MainModule {
}