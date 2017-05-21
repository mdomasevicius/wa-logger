package lt.platform.lunar.logger

import com.github.javafaker.Faker
import lt.platform.lunar.App
import lt.platform.lunar.RestClient
import lt.platform.lunar.logger.celebrities.CelebrityResource
import lt.platform.lunar.logger.url.CrawlURLResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static lt.platform.lunar.RestClient.createdResourcePath

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = App)
class CelebritySpec extends Specification {

    @Autowired
    RestClient restClient

    def faker = new Faker()

    def 'can not log celebrity for unknown url'() {
        when:
            def response = restClient.post('/api/29432/celebrities', new CelebrityResource(
                firstName: 'na',
                lastName: 'na',
                address: 'na'
            ))
        then:
            response.statusCode.value() == 404
    }

    def 'can log celebrity'() {
        given:
            def urlCreateResponse = restClient.post('/api/url', new CrawlURLResource(url: 'http://celeb.gawker.af'))

        when:
            def createResponse = restClient.post(
                "${createdResourcePath(urlCreateResponse)}/celebrities",
                new CelebrityResource(
                    firstName: 'Bruce',
                    lastName: 'Willis',
                    address: 'Hollywod St. 100'
                ))
        then:
            createResponse.statusCode.value() == 200

        when:
            def celebrityResponse = restClient.get("${createdResourcePath(urlCreateResponse)}/celebrities")
        then:
            celebrityResponse.statusCode.value() == 200
            celebrityResponse.body.entries.find {
                it.firstName == 'Bruce' && it.lastName == 'Willis' && it.address == 'Hollywod St. 100'
            }
    }

    def 'can retrieve all celebrities by url'() {
        given:
            def urlCreateResponse = restClient.post('/api/url', new CrawlURLResource(url: 'http://list.of.celebs.de'))
            10.times {
                restClient.post("${createdResourcePath(urlCreateResponse)}/celebrities", new CelebrityResource(
                    firstName: faker.name().firstName(),
                    lastName: faker.name().lastName(),
                    address: faker.address().fullAddress()
                ))
            }
        when:
            def response = restClient.get("${createdResourcePath(urlCreateResponse)}/celebrities")
        then:
            response.statusCode.value() == 200
            response.body.entries.size() == 10
    }
}
