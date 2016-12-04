var ioc = {
    dataSource: {
        type: "com.alibaba.druid.pool.DruidDataSource",
        events: {
            depose: "close"
        },
        fields: {
            driverClassName: 'com.mysql.jdbc.Driver',
            url: 'jdbc:mysql://localhost:3306/yiduihuan?useUnicode=true&characterEncoding=UTF-8&useSSL=false',
            username: 'yiduihuan',
            password: 'SfqME/0xEe0C0i8YEEQ/c/xAjkXEe00jxvnE+Wf7EgnX9ZHS5VDwSwy00dgxO/0E0y4rij2GCXeLfE/GaiMRQQ==',
            initialSize: 1,
            maxActive: 500,
            minIdle: 1,
            filters: 'config',
            connectionProperties: 'config.decrypt=true;config.decrypt.key=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJha56yd28ghN0Pt35JJs1WOO/f+0++tMeC3d1IPgDlqk+4J28UpDyHhb4r3s6DiUklZFC9UI/vpFBCT52lnxEUCAwEAAQ==',
            defaultAutoCommit: false,

            validationQuery: "select 1"
        }
    },
    dao: {
        type: "org.nutz.dao.impl.NutDao",
        args: [{refer: "dataSource"}],
        fields: {
            executor: {refer: "cacheExecutor"}
        }
    },
    cacheExecutor: {
        type: "org.nutz.plugins.cache.dao.CachedNutDaoExecutor",
        fields: {
            cacheProvider: {refer: "cacheProvider"},
            cachedTableNames: [ // 任意N个表
                "tb_yiduihuan_help",
                "tb_yiduihuan_notice",
                "tb_yiduihuan_help_category",
                "tb_yiduihuan_explain"
            ],
            //cachedTableNamePatten : ".+" // 也可以通过正则表达式来匹配
        }
    },
    cacheProvider: {
        type: "org.nutz.plugins.cache.dao.impl.provider.MemoryDaoCacheProvider",
        fields: {
            cacheSize: 10000 // 缓存的对象数
        },
        events: {
            create: "init"
        }
    }
};