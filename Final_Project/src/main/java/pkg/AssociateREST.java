/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author c0710955
 */
@Path("associate")
@ApplicationScoped
public class AssociateREST {
    @PersistenceContext(unitName = "com.mycompany_Final_Project_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Inject
    private UserTransaction transaction;

    // http://localhost:8080/Final_Project/api/associate
    /**
     * Uses a JPA Query to return the entire list as JSON.
     * @return List of Users
     */
    @GET
    @Produces({"application/json"})
    public List<Associate> getAll() {
        List<Associate> associates = em.createQuery("SELECT a FROM Associate a").getResultList();
        return associates;
    }

    /**
     * Uses a JPA Named Query to return a specific entity as a list.
     * @param id the Associate Id (ID)
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public List<Associate> getOne(@PathParam("id") String id) {
        Query q = em.createNamedQuery("a.findOne");
        q.setParameter("associateId", id);
        List<Associate> associates = q.getResultList();
        return associates;
    }

    /**
     * Saves an object received as a JSON payload.
     * @param associate 
     */
    @POST
    @Consumes("application/json")
    public void addOne(Associate associate) {
        try {
            transaction.begin();
            em.persist(associate);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(AssociateREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates an existing Product Code based on an incoming JSON payload.
     * @param associate
     * @param id 
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void editOne(Associate associate, @PathParam("id") String id) {
        try {
            Query q = em.createQuery("SELECT a FROM Associate a WHERE a.associateId = :id");
            q.setParameter("id", id);
            Associate savedPC = (Associate) q.getSingleResult();
            savedPC.setUsername(associate.getUsername());
            savedPC.setPassword(associate.getPassword());
            savedPC.setfName(associate.getfName());
            savedPC.setlName(associate.getlName());
            transaction.begin();
            em.merge(savedPC);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(AssociateREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Finds and deletes an existing record using the JPA's find method.
     * @param id 
     */
    @DELETE
    @Path("{id}")
    public void deleteOne(@PathParam("id") String id) {
        try {
            transaction.begin();
            Associate found = em.find(Associate.class, id);
            em.remove(found);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(AssociateREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
