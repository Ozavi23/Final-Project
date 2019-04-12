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
@Path("person")
@ApplicationScoped
public class UserREST {
    
    @PersistenceContext(unitName = "com.mycompany_Final_Project_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    
    @Inject
    private UserTransaction transaction;

    // http://localhost:8080/Final_Project/api/user
    /**
     * Uses a JPA Query to return the entire list as JSON.
     * @return List of Users
     */
    @GET
    @Produces({"application/json"})
    public List<User> getAll() {
        List<User> users = em.createQuery("SELECT u FROM Person u").getResultList();
        return users;
    }

    /**
     * Uses a JPA Named Query to return a specific entity as a list.
     * @param id the Product Code (ID)
     * @return 
     */
    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public List<User> getOne(@PathParam("id") String id) {
        Query q = em.createNamedQuery("u.findOne");
        q.setParameter("id", id);
        List<User> users = q.getResultList();
        return users;
    }

    /**
     * Saves an object received as a JSON payload.
     * @param user 
     */
    @POST
    @Consumes("application/json")
    public void addOne(User user) {
        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(UserREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Updates an existing Product Code based on an incoming JSON payload.
     * @param user
     * @param id 
     */
    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void editOne(User user, @PathParam("id") String id) {
        try {
            Query q = em.createQuery("SELECT u FROM Person u WHERE u.userId = :id");
            q.setParameter("id", id);
            User savedPC = (User) q.getSingleResult();
            savedPC.setUsername(user.getUsername());
            savedPC.setPassword(user.getPassword());
            transaction.begin();
            em.merge(savedPC);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(UserREST.class.getName()).log(Level.SEVERE, null, ex);
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
            User found = em.find(User.class, id);
            em.remove(found);
            transaction.commit();
        } catch (Exception ex) {
            Logger.getLogger(UserREST.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
