package org.example.apicalls.apiconfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.example.api.data.entity.Account;
import org.example.api.data.entity.Card;
import org.example.api.data.entity.Customer;
import org.example.api.data.request.LoginRequest;
import org.example.api.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BankAPI {

    // AÑADIR TODOS LOS ENDPOINTS DE NUESTRA API

    /*
    @GET
    @Path("/v1/storage/files")
    @Produces(MediaType.APPLICATION_JSON)
    Response getV1StorageFiles(
            @HeaderParam("Authorization") String authorization, @QueryParam("q") String appId, @QueryParam("kind") String kind, @QueryParam("per_page") Integer perPage);

    @GET
    @Path("/rest/v1/info/platforms/{automation_api}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getV1InfoPlatformsAutomationApi(
            @HeaderParam("Authorization") String authorization,
            @PathParam("automation_api") String automation_api);
     */

    @GET
    @Path("/api/account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response accountById(
            @PathParam("id") Integer id);

    @GET
    @Path("/api/accounts/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response accountsByCustomer(
            @PathParam("customerId") Integer customerId);

    @GET
    @Path("/api/accounts")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserAccounts(@Context HttpServletRequest request);

    @GET
    @Path("/api/accounts/amount")
    @Produces(MediaType.APPLICATION_JSON)
    Response getUserAmount(@Context HttpServletRequest request);

    @GET
    @Path("/api/amount/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response amountOfAccount(@PathParam("accountId") Integer accountId);

    @POST
    @Path("/api/account/new")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response createAccount(Account newAccount, @Context HttpServletRequest request);

    @POST
    @Path("/public/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response addCustomer(Customer nuevoCust);

    @POST
    @Path("/public/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    Response login(LoginRequest logInRequest, @Context HttpServletRequest request);

    @POST
    @Path("/public/logout")
    @Produces(MediaType.TEXT_PLAIN)
    Response logout(@Context HttpServletRequest request);

    @GET
    @Path("/api/BDcards")
    @Produces(MediaType.APPLICATION_JSON)
    List<Card> getAllCards();

    @GET
    @Path("/api/card/{cardId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getCardById(@PathParam("cardId") Integer cardId);

    @GET
    @Path("/api/cards/{accountId}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getCardsByAccountId(@PathParam("accountId") Integer accountId);

}

