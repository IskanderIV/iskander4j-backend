import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder

import java.nio.charset.Charset

def appenderList = ["CONSOLE"]
def appName      = System.getenv("SERVICE_NAME")  ?: "iskander4j-backend"

Level rootLogLevel   = valueOf(System.getenv("LOG_LEVEL")        ?: 'INFO')
Level springLogLevel = valueOf(System.getenv("SPRING_LOG_LEVEL") ?: 'debug')

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = Charset.forName("UTF-8")
        pattern = '%-4relative %d %-5level [ %t ] [%mdc{X-B3-TraceId},%mdc{X-B3-SpanId}] %-55logger{13} | %m %n'
    }
}

logger('org.springframework', springLogLevel)
root(rootLogLevel, appenderList)

println '=' * 80
println """
    APP INSTANCE NAME     : $appName
    LOGGING ROOT LEVEL    : $rootLogLevel
    LOGGING SPRING LEVEL  : $springLogLevel
    APPENDERS             : $appenderList
"""
println '=' * 80