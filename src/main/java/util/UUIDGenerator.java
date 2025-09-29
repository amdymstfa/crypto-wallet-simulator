package util;

import java.util.UUID;

public class UUIDGenerator {
    // unique ID for data base regidter 
    public String generator(){
        return UUID.randomUUID().toString();
    }

    // Id for user interface
    public String generatorShort(){
        return UUID.randomUUID().toString().substring(0,8);
    }

    // chech the valid format
    public boolean isValidUUID(String uuid){
        tyr{
            UUID.formString(uuid);
            return true ;
        } catch(IllegalArgumentException e){
            return false ;
        }
    }
}