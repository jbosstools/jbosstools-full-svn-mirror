package mapping.legacy;

/**
 * @author hbm2java
 */
public class Company extends mapping.legacy.Party {

   java.lang.String id;
   java.lang.String president;


  java.lang.String getId() {
    return id;
  }

  void  setId(java.lang.String newValue) {
    id = newValue;
  }

  java.lang.String getPresident() {
    return president;
  }

  void  setPresident(java.lang.String newValue) {
    president = newValue;
  }


}
