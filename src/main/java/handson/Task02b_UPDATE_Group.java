package handson;

import com.commercetools.api.client.ApiRoot;
import handson.impl.ApiPrefixHelper;
import handson.impl.ClientService;
import handson.impl.CustomerService;
import io.vrap.rmf.base.client.ApiHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static handson.impl.ClientService.createApiClient;
import static handson.impl.ClientService.getProjectKey;

/**
 * Configure sphere client and get project information.
 *
 * See:
 *  TODO dev.properties
 *  TODO {@link ClientService#createApiClient(String prefix)}
 */
public class Task02b_UPDATE_Group {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        final String apiClientPrefix = ApiPrefixHelper.API_DEV_CLIENT_PREFIX.getPrefix();
        Logger logger = LoggerFactory.getLogger(Task02b_UPDATE_Group.class.getName());
        final ApiRoot client = createApiClient(apiClientPrefix);
        CustomerService customerService = new CustomerService(client, getProjectKey(apiClientPrefix));

        try (ApiHttpClient apiHttpClient = ClientService.apiHttpClient) {
            // TODO:
            //  GET a customer
            //  GET a customer group
            //  ASSIGN the customer to the customer group
            //
            logger.info("Customer assigned to group: " +
                    customerService.getCustomerByKey("sandeeptestcom")
                            .thenCombineAsync(
                                    customerService.getCustomerGroupByKey("indoor-customer-group"),
                                    customerService::assignCustomerToCustomerGroup
                            ).thenComposeAsync(CompletableFuture::toCompletableFuture)
                            .toCompletableFuture().get().getBody().getKey()
            );
        }
    }

}

