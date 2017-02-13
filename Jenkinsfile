@Library('libpipelines@master') _

hose {
    MAIL = 'governance'
    LANG = 'scala'
    SLACKTEAM = 'data-governance'
    MODULE = 'tiki-takka'
    REPOSITORY = 'tiki-takka'
    DEVTIMEOUT = 30
    RELEASETIMEOUT = 30
    MAXITRETRIES = 2

    ITSERVICES = [
        ['CONSUL': [
            'image': 'consul:0.7.0',
            'sleep': 20,
            'healthcheck': 8500
        ]]
    ]

    ITPARAMETERS = """
        | -Dconsul.uri="http://%%CONSUL:8500"
        | """

    DEV = {
        config ->
            doCompile(config)
            parallel(UT: {doUT(config)},
                     IT: {doIT(config)},
                     failFast: config.FAILFAST)

            doPackage(config)

            parallel(QC: {doStaticAnalysis(config)},
                     DEPLOY: {doDeploy(config)},
                     failFast: config.FAILFAST)
    }
}
