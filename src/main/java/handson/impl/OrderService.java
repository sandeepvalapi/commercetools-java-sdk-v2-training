package handson.impl;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.order.*;
import com.commercetools.api.models.state.State;
import com.commercetools.api.models.state.StateResourceIdentifierBuilder;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

/**
 * This class provides operations to work with {@link Order}s.
 */
public class OrderService {

    ApiRoot apiRoot;
    String projectKey;

    public OrderService(final ApiRoot client, String projectKey) {
        this.apiRoot = client;
        this.projectKey = projectKey;
    }

    public CompletableFuture<ApiHttpResponse<Order>> createOrder(final ApiHttpResponse<Cart> cartApiHttpResponse) {

           final Cart cart = cartApiHttpResponse.getBody();
            return apiRoot.withProjectKey(projectKey
            ).orders().post(
                    OrderFromCartDraftBuilder.of()
                    .version(cart.getVersion())
                    .id(cart.getId())
                    .build()
            ).execute();
    }


    public CompletableFuture<ApiHttpResponse<Order>> changeState(
            final ApiHttpResponse<Order> orderApiHttpResponse,
            final OrderState state) {

       return null;
    }


    public CompletableFuture<ApiHttpResponse<Order>> changeWorkflowState(
            final ApiHttpResponse<Order> orderApiHttpResponse,
            final State workflowState) {

        return null;
    }

}
