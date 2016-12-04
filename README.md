# Nutz版CMS

用nutz好多年了，都答应兽总（@Wendal）好多次要把做的东西拿出来做个demo，最近在一群Nutz&XBlink 58444676，有新人提出来为什么没Nutz版的CMS放出来。作为天天骚扰兽总还是要给点回报的那，所以将自己项目中的CMS部分分离出来，当nutz的入门参考，代码烂，有bug提交issue。

###框架 nutz
Nutz项目主页<https://github.com/nutzam/nutz>

Nutz社区<https://nutz.cn/>

Nutz官网<https://nutzam.com/>

这东西我就不介绍了，不知道就不用看下面的东西了。




###数据库连接池 druid
项目地址<https://github.com/alibaba/druid>

/con/ioc/dao.js内密码加密方式
在命令行中执行如下命令
```javascript
java -cp druid-1.0.27.jar com.alibaba.druid.filter.config.ConfigTools you_password
```
输出
```javascript
privateKey:MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEA6+4avFnQKP+O7bu5YnxWoOZjv3no4aFV558HTPDoXs6EGD0HP7RzzhGPOKmpLQ1BbA5viSht+aDdaxXp6SvtMQIDAQABAkAeQt4fBo4SlCTrDUcMANLDtIlax/I87oqsONOg5M2JS0jNSbZuAXDv7/YEGEtMKuIESBZh7pvVG8FV531/fyOZAiEA+POkE+QwVbUfGyeugR6IGvnt4yeOwkC3bUoATScsN98CIQDynBXC8YngDNwZ62QPX+ONpqCel6g8NO9VKC+ETaS87wIhAKRouxZL38PqfqV/WlZ5ZGd0YS9gA360IK8zbOmHEkO/AiEAsES3iuvzQNYXFL3x9Tm2GzT1fkSx9wx+12BbJcVD7AECIQCD3Tv9S+AgRhQoNcuaSDNluVrL/B/wOmJRLqaOVJLQGg==
publicKey:MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOvuGrxZ0Cj/ju27uWJ8VqDmY7956OGhVeefB0zw6F7OhBg9Bz+0c84RjzipqS0NQWwOb4kobfmg3WsV6ekr7TECAwEAAQ==
password:PNak4Yui0+2Ft6JSoKBsgNPl+A033rdLhFw+L0np1o+HDRrCo9VkCuiiXviEMYwUgpHZUFxb2FpE0YmSguuRww==
```
将输出结果填写到dao.js的password与connectionProperties内的config.decrypt.key
```javascript
fields: {
            driverClassName:'com.mysql.jdbc.Driver',
            url:'jdbc:mysql://localhost:3306/yiduihuan?useUnicode=true&characterEncoding=UTF-8&useSSL=false',
            username:'yiduihuan',
            password:'SfqME/0xEe0C0i8YEEQ/c/xAjkXEe00jxvnE+Wf7EgnX9ZHS5VDwSwy00dgxO/0E0y4rij2GCXeLfE/GaiMRQQ==',
            initialSize:1,
            maxActive:500,
            minIdle:1,
            filters:'config',
            connectionProperties:'config.decrypt=true;config.decrypt.key=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJha56yd28ghN0Pt35JJs1WOO/f+0++tMeC3d1IPgDlqk+4J28UpDyHhb4r3s6DiUklZFC9UI/vpFBCT52lnxEUCAwEAAQ==',
            defaultAutoCommit:false,
        }
```

###数据库Percona Server
项目地址<https://www.percona.com/doc/percona-server/5.7/index.html>

此为非原生MySQL，数据库脚本在/sql/文件夹中


###Web服务器Nginx 
项目地址<http://nginx.org/>

nginx配置文件在/nginx/文件夹中，建议使用https，具体请自行搜索。

###模板引擎 Beetl
项目地址<http://ibeetl.com/>

用这个主要是beetl模板引擎原生支持nutz，具体的自己看看文档。

###工具类 hutool
项目地址<http://hutool.mydoc.io/>


#####有bug欢迎issue，有问题去<https://nutz.cn>提问，demo效果见<https://www.yiduihuan.com/>。