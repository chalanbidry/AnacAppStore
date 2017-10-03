/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ged;

/**
 *
 * @author xess
 */
public class CmisOperationException extends Exception {
    public CmisOperationException(String message) {
        super(message);
    }
    
    public CmisOperationException(String message, Throwable t) {
        super(message, t);
    }
}
