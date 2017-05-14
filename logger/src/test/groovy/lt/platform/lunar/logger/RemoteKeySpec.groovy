package lt.platform.lunar.logger

import lt.platform.lunar.App
import lt.platform.lunar.RestClient
import lt.platform.lunar.logger.key.RemoteKeyResource
import lt.platform.lunar.logger.url.CrawlURLResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.util.concurrent.ThreadLocalRandom

import static java.lang.Long.MAX_VALUE
import static lt.platform.lunar.RestClient.createdResourcePath

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App)
class RemoteKeySpec extends Specification {

    @Autowired
    RestClient restClient

    def 'remote key creation requires url present'() {
        given:
            def magicUrlId = ThreadLocalRandom.current().nextLong(MAX_VALUE)
        when:
            def response = restClient.post(
                "/api/url/$magicUrlId/remote-key",
                new RemoteKeyResource('test'))
        then:
            response
            response.statusCode.value() == 404
    }

    def 'can create remote key for existing url'() {
        given:
            def urlCreateResponse = restClient.post(
                "/api/url",
                new CrawlURLResource(url: 'http://google.lt'))
        when:
            def response = restClient.post(
                "${createdResourcePath(urlCreateResponse)}/remote-key",
                new RemoteKeyResource('test'))
        then:
            response
            response.statusCode.value() == 201
            response.headers.Location
    }

    def 'can retrieve created remote key'() {
        given:
            def urlCreateResponse = restClient.post(
                "/api/url",
                new CrawlURLResource(url: 'http://test.lt'))

            def keyCreateResponse = restClient.post(
                "${createdResourcePath(urlCreateResponse)}/remote-key",
                new RemoteKeyResource('test'))
        when:
            def response = restClient.get(createdResourcePath(keyCreateResponse))
        then:
            response
            response.statusCode.value() == 200
            response.body.remoteKey
    }
}
