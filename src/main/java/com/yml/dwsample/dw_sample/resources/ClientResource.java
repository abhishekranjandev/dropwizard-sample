package com.yml.dwsample.dw_sample.resources;

import java.io.IOException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;

import javax.ws.rs.core.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yml.dwsample.dw_sample.auth.User;
import com.yml.dwsample.dw_sample.representations.Contact;

import io.dropwizard.auth.Auth;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.TEXT_PLAIN)
@Path("/client/")
public class ClientResource {

	private Client client;
	
	private static ObjectMapper mapper; 
	  
	 static { 
	  mapper = new ObjectMapper(); 
	 
	 } 

	public ClientResource(Client client) {
		this.client = client;
	}

	@GET
	@Path("showContact")
	@PermitAll
	public String showContact(@QueryParam("id") int id,@Auth User user) {
		WebTarget  contactResource = client.target("http://localhost:8080/contact/" + id);
		Invocation.Builder invocationBuilder =  contactResource.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
		Contact c = response.readEntity(Contact.class);
		String output = "ID: " + id + "\nFirst name: " + c.getFirstName() + "\nLast name: " + c.getLastName()
				+ "\nPhone: " + c.getPhone();
		return output;
	}

	@PermitAll
	@GET
	@Path("newContact")
	public Response newContact(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
			@QueryParam("phone") String phone,@Auth User user) throws IOException {
		WebTarget contactResource = client.target("http://localhost:8080/contact");
		Invocation.Builder invocationBuilder =  contactResource.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(serialize(new Contact(0, firstName, lastName, phone)),MediaType.APPLICATION_JSON_TYPE));
		
		if (response.getStatus() == 201) {
			// Created
			return Response.status(302).entity("The contact was created successfully! The new contact can be found at "
					+ response.getHeaders().getFirst("Location")).build();
		} else {
			// Other Status code, indicates an error
			return Response.status(422).entity(response.readEntity(String.class)).build();
		}
	}

	@PermitAll
	@GET
	@Path("updateContact")
	public Response updateContact(@QueryParam("id") int id, @QueryParam("firstName") String firstName,
			@QueryParam("lastName") String lastName, @QueryParam("phone") String phone,@Auth User user) throws IOException {
		WebTarget contactResource = client.target("http://localhost:8080/contact/" + id);
		Invocation.Builder invocationBuilder =  contactResource.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.put(
				Entity.entity(serialize(new Contact(id, firstName, lastName, phone)),MediaType.APPLICATION_JSON_TYPE));
		if (response.getStatus() == 200) {
			// Created
			return Response.status(302).entity("The contact was updated successfully!").build();
		} else {
			// Other Status code, indicates an error
			return Response.status(422).entity(response.readEntity(String.class)).build();
		}
	}

	@RolesAllowed({ "ADMIN" })
	@GET
	@Path("deleteContact")
	public Response deleteContact(@QueryParam("id") int id,@Auth User user) {
		WebTarget contactResource = client.target("http://localhost:8080/contact/" + id);
		Invocation.Builder invocationBuilder =  contactResource.request(MediaType.APPLICATION_JSON);
		invocationBuilder.delete();
		return Response.noContent().entity("Contact was deleted!").build();
	}
	
	public static String serialize(final Contact contact) throws IOException { 
		  return mapper.writeValueAsString(contact); 
		 } 
}
