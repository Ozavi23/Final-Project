/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 *
 * @author c0710955
 */
public class Post {
    @Id
    @Column(name = "POST_ID")
    private int postId;
    @Column(name = "DESCRIPTION")
    private String desciption;
    @Column(name = "POST_DATE")
    private String postDate;
    
    @Column(name = "USER_ID")
    private int userId;
}
