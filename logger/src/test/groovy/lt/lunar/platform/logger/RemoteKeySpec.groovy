package lt.lunar.platform.logger

import lt.lunar.platform.App
import lt.lunar.platform.RestClient
import lt.lunar.platform.logger.key.RemoteKeyResource
import lt.lunar.platform.logger.url.CrawlURLResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.util.concurrent.ThreadLocalRandom

import static java.lang.Long.*
import static lt.lunar.platform.RestClient.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [App])
class RemoteKeySpec extends Specification {

    @Autowired
    RestClient restClient

    def 'remote key creation requires url present'() {
        given:
            def magicUrlId = ThreadLocalRandom.current().nextLong(MAX_VALUE)
        when:
            def response = restClient.post(
                "/api/url/$magicUrlId/remote-key",
                new RemoteKeyResource('test'),
                ['Crawler-Id': 'ra'])
        then:
            response
            response.statusCode.value() == 404
    }

    def 'can create remote key for existing url'() {
        given:
            def urlCreateResponse = restClient.post(
                "/api/url",
                new CrawlURLResource(url: 'http://google.lt'),
                ['Crawler-Id': 'ra'])
        when:
            def response = restClient.post(
                "${createdResourcePath(urlCreateResponse)}/remote-key",
                new RemoteKeyResource('test'),
                ['Crawler-Id': 'ra'])
        then:
            response
            response.statusCode.value() == 201
            response.headers.Location
    }

    def 'can retrieve created remote key'() {
        given:
            def urlCreateResponse = restClient.post(
                "/api/url",
                new CrawlURLResource(url: 'http://test.lt'),
                ['Crawler-Id': 'ra'])

            def keyCreateResponse = restClient.post(
                "${createdResourcePath(urlCreateResponse)}/remote-key",
                new RemoteKeyResource('test'),
                ['Crawler-Id': 'ra'])
        when:
            def response = restClient.get(createdResourcePath(keyCreateResponse), ['Crawler-Id': 'ra'])
        then:
            response
            response.statusCode.value() == 200
            response.body.remoteKey
    }
}
