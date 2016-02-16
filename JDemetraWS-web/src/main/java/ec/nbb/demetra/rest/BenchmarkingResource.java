/*
 * Copyright 2014 National Bank of Belgium
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved 
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and 
 * limitations under the Licence.
 */
package ec.nbb.demetra.rest;

import ec.benchmarking.simplets.TsCholette;
import ec.benchmarking.simplets.TsDenton;
import ec.benchmarking.simplets.TsDenton2;
import ec.benchmarking.simplets.TsExpander;
import ec.nbb.demetra.filter.Compress;
import ec.nbb.demetra.json.JsonTsData;
import ec.nbb.demetra.json.benchmarking.JsonCholetteProcessing;
import ec.nbb.demetra.json.benchmarking.JsonDentonProcessing;
import ec.nbb.demetra.json.benchmarking.JsonExpanderProcessing;
import ec.nbb.demetra.json.benchmarking.JsonSsfDentonProcessing;
import ec.tstoolkit.timeseries.simplets.TsData;
import ec.tstoolkit.timeseries.simplets.TsFrequency;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Mats Maggi
 */
@Path("/benchmarking")
@Api(value = "/benchmarking", hidden = false)
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class BenchmarkingResource {

    @POST
    @Compress
    @Path("/denton")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Denton processing", notes = "Computes a Denton Processing on a given series", response = JsonTsData.class)
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Denton processing successfully done", response = JsonTsData.class),
                @ApiResponse(code = 400, message = "Bad request", response = String.class),
                @ApiResponse(code = 500, message = "Invalid request", response = String.class)
            }
    )
    public Response dentonProcessing(@ApiParam(value = "denton", required = true) JsonDentonProcessing dp) {
        TsDenton2 denton = new TsDenton2();
        denton.setAggregationType(dp.agg);
        denton.setDifferencingOrder(dp.differencing);
        denton.setModified(dp.modified);
        denton.setMultiplicative(dp.mul);
        
        TsData xbench;
        if (dp.x == null) {
            denton.setDefaultFrequency(dp.defaultFrequency);
            xbench = denton.process(null, dp.y.create());
        } else {
            xbench = denton.process(dp.x.create(), dp.y.create());
        }
        if (xbench != null) {
            JsonTsData result = new JsonTsData();
            result.from(xbench);
            return Response.ok().entity(result).build();
        } else {
            throw new IllegalArgumentException("Denton processing returned a null result !");
        }
    }

    @POST
    @Compress
    @Path("/ssfdenton")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Ssf Denton processing", notes = "Computes a Ssf Denton Processing on a given series", response = JsonTsData.class)
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Ssf Denton processing successfully done", response = JsonTsData.class),
                @ApiResponse(code = 400, message = "Bad request", response = String.class),
                @ApiResponse(code = 500, message = "Invalid request", response = String.class)
            }
    )
    public Response ssfDentonProcessing(@ApiParam(value = "ssfdenton", required = true) JsonSsfDentonProcessing dp) {
        TsDenton denton = new TsDenton();
        denton.setAggregationType(dp.agg);
        denton.setMultiplicative(dp.mul);
        TsData xbench = denton.process(dp.x.create(), dp.y.create());

        if (xbench != null) {
            JsonTsData result = new JsonTsData();
            result.from(xbench);
            return Response.ok().entity(result).build();
        } else {
            throw new IllegalArgumentException("Ssf Denton processing returned a null result !");
        }
    }

    @POST
    @Compress
    @Path("/cholette")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Cholette processing", notes = "Computes a Cholette Processing on a given series", response = JsonTsData.class)
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Cholette processing successfully done", response = JsonTsData.class),
                @ApiResponse(code = 400, message = "Bad request", response = String.class),
                @ApiResponse(code = 500, message = "Invalid request", response = String.class)
            }
    )
    public Response choletteProcessing(@ApiParam(value = "cholette", required = true) JsonCholetteProcessing cho) {
        TsCholette cholette = new TsCholette();
        cholette.setAggregationType(cho.agg);
        cholette.setRho(cho.rho);
        cholette.setLambda(cho.lambda);
        cholette.setBiasCorrection(cho.bias);
        TsData xbench = cholette.process(cho.x.create(), cho.y.create());

        if (xbench != null) {
            JsonTsData result = new JsonTsData();
            result.from(xbench);
            return Response.ok().entity(result).build();
        } else {
            throw new IllegalArgumentException("Cholette processing returned a null result !");
        }
    }

    @POST
    @Compress
    @Path("/expander")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Expander processing", notes = "Computes a Expander Processing on a given series", response = JsonTsData.class)
    @ApiResponses(
            value = {
                @ApiResponse(code = 200, message = "Expander processing successfully done", response = JsonTsData.class),
                @ApiResponse(code = 400, message = "Bad request", response = String.class),
                @ApiResponse(code = 500, message = "Invalid request", response = String.class)
            }
    )
    public Response choletteProcessing(@ApiParam(value = "expander", required = true) JsonExpanderProcessing exp) {
        TsExpander expander = new TsExpander();
        expander.setType(exp.agg);
        expander.setModel(exp.model);
        if (exp.useparam) {
            expander.setParameter(exp.parameter);
            expander.estimateParameter(false);
        } else {
            expander.estimateParameter(true);
        }
        expander.useConst(exp.constant);
        expander.useTrend(exp.trend);

        TsData xbench;
        if (exp.domain != null) {
            xbench = expander.expand(exp.y.create(), exp.domain.create());
        } else {
            xbench = expander.expand(exp.y.create(), TsFrequency.valueOf(exp.defaultFrequency));
        }

        if (xbench != null) {
            JsonTsData result = new JsonTsData();
            result.from(xbench);
            return Response.ok().entity(result).build();
        } else {
            throw new IllegalArgumentException("Expander processing returned a null result !");
        }
    }
}