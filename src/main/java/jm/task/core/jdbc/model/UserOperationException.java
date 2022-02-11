/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jm.task.core.jdbc.model;

/**
 *
 * @author keinn
 */
public class UserOperationException extends RuntimeException {

    public UserOperationException(String operation) {
        this(operation, null);
    }

    public UserOperationException(String operation, Throwable t) {
        super("Error in " + operation + " of User", t);
    }

}
