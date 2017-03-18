package com.trizic.api.service.v1;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("v1/advisor")
public interface AdvisorResource {

    @PUT
    @Path("{advisorId}/model")
    @Consumes("application/json")
    @Produces("application/json")
    @Valid
    ModelResponse save(
            @PathParam("advisorId") @NotNull String advisorId,
            @Valid @NotNull @ValidAllocation Model entity
        );


    @GET
    @Path("{advisorId}/model")
    @Produces("application/json")
    @Valid
    ModelPage fetch(
            @PathParam("advisorId") @NotNull String advisorId,
            @QueryParam("pageNumber") @Min(1) @DefaultValue("1") Integer pageNumber,
            @QueryParam("pageSize") @Min(1) @DefaultValue("20") Integer pageSize
        );
}
