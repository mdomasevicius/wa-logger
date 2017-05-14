package lt.lunar.platform.logger

import com.github.javafaker.Faker
import lt.lunar.platform.App
import lt.lunar.platform.RestClient
import lt.lunar.platform.logger.celebrities.CelebrityResource
import lt.lunar.platform.logger.url.CrawlURLResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static lt.lunar.platform.RestClient.createdResourcePath

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App)
class CelebritySpec extends Specification {

    @Autowired
    RestClient restClient

    def faker = new Faker()

    def 'can not log celebrity for unknown url'() {
        when:
            def response = restClient.post('/api/celebrities', new CelebrityResource(
                sourceUrl: 'http://unknown',
                firstName: 'na',
                lastName: 'na',
                address: 'na'
            ))
        then:
            response.statusCode.value() == 404
    }

    def 'can log celebrity'() {
        given:
            restClient.post('/api/url', new CrawlURLResource(url: 'http://celeb.gawker.af'))

        when:
            def createResponse = restClient.post('/api/celebrities', new CelebrityResource(
                sourceUrl: 'http://celeb.gawker.af',
                firstName: 'Bruce',
                lastName: 'Willis',
                address: 'Hollywod St. 100'
            ))
        then:
            createResponse.statusCode.value() == 201
            createResponse.headers.Location

        when:
            def celebrityResponse = restClient.get(createdResourcePath(createResponse))
        then:
            celebrityResponse.statusCode.value() == 200
            with(celebrityResponse.body) {
                it.sourceUrl == 'http://celeb.gawker.af'
                it.firstName == 'Bruce'
                it.lastName == 'Willis'
                it.address == 'Hollywod St. 100'
            }
    }

    def 'can retrieve all celebrities by url'() {
        given:
            restClient.post('/api/url', new CrawlURLResource(url: 'http://list.of.celebs.de'))
            10.times {
                restClient.post('/api/celebrities', new CelebrityResource(
                    sourceUrl: 'http://list.of.celebs.de',
                    firstName: faker.name().firstName(),
                    lastName: faker.name().lastName(),
                    address: faker.address().fullAddress()
                ))
            }
        when:
            def response = restClient.get("/api/celebrities?url=http://list.of.celebs.de")
        then:
            response.statusCode.value() == 200
            response.body.entries.size() == 10
            with(response.body.entries[4]) {
                it.sourceUrl
                it.firstName
                it.lastName
                it.address
            }
    }
}
