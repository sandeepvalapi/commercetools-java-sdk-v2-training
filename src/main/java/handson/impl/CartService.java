package handson.impl;

import com.commercetools.api.client.ApiRoot;
import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.channel.Channel;
import com.commercetools.api.models.channel.ChannelResourceIdentifierBuilder;
import com.commercetools.api.models.customer.Customer;
import io.vrap.rmf.base.client.ApiHttpResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class CartService {

    ApiRoot apiRoot;
    String projectKey;

    public CartService(final ApiRoot client, String projectKey) {
        this.apiRoot = client;
        this.projectKey = projectKey;
    }


    /**
     * Creates a cart for the given customer.
     *
     * @return the customer creation completion stage
     */
    public CompletableFuture<ApiHttpResponse<Cart>> createCart(final ApiHttpResponse<Customer> customerApiHttpResponse) {

        Customer customer = customerApiHttpResponse.getBody();
        return apiRoot.withProjectKey(projectKey)
                .carts()
                .post(
                        CartDraftBuilder.of()
                                .currency("INR")
                                .inventoryMode(InventoryMode.TRACK_ONLY)
                                .customerEmail(customer.getEmail())
                                .customerId(customer.getId())
                                .deleteDaysAfterLastModification(90L)
                                .shippingAddress(
                                        customer
                                                .getAddresses()
                                                .stream()
                                                .filter(a -> a.getId().equals(customer.getDefaultShippingAddressId()))
                                                .findFirst()
                                                .get()
                                )
                                .country(
                                        customer
                                                .getAddresses()
                                                .stream()
                                                .filter(a -> a.getId().equals(customer.getDefaultShippingAddressId()))
                                                .findFirst()
                                                .get()
                                                .getCountry())
                                .build()
                ).execute();
    }


    public CompletableFuture<ApiHttpResponse<Cart>> createAnonymousCart() {

        return
                apiRoot
                        .withProjectKey(projectKey)
                        .carts()
                        .post(
                                CartDraftBuilder.of()
                                        .currency("EUR")
                                        .deleteDaysAfterLastModification(90L)
                                        .anonymousId("an" + System.nanoTime())
                                        .country("DE")
                                        .build()
                        )
                        .execute();
    }


    public CompletableFuture<ApiHttpResponse<Cart>> addProductToCartBySkusAndChannel(
            final ApiHttpResponse<Cart> cartApiHttpResponse,
            final Channel channel,
            final String... skus) {

        Cart cart = cartApiHttpResponse.getBody();
        List<CartUpdateAction> cartUpdateActions = Stream.of(skus)
                .map(sku -> CartAddLineItemActionBuilder.of()
                        .sku(sku)
                        .quantity(1L)
                        .supplyChannel(
                                ChannelResourceIdentifierBuilder.of()
                                        .id(channel.getId())
                                        .build()
                        ).build()
                ).collect(Collectors.toList());

        return apiRoot.withProjectKey(projectKey)
                .carts()
                .withId(cart.getId())
                .post(
                        CartUpdateBuilder.of()
                                .version(cart.getVersion())
                                .actions(
                                        //Want list of cart update actions
                                        cartUpdateActions
                                )
                                .build()
                ).execute();
    }

    public CompletableFuture<ApiHttpResponse<Cart>> addDiscountToCart(
            final ApiHttpResponse<Cart> cartApiHttpResponse, final String code) {

        return null;
    }

    public CompletableFuture<ApiHttpResponse<Cart>> recalculate(final ApiHttpResponse<Cart> cartApiHttpResponse) {

        return null;
    }

    public CompletableFuture<ApiHttpResponse<Cart>> setShipping(final ApiHttpResponse<Cart> cartApiHttpResponse) {

        return null;
    }


}
