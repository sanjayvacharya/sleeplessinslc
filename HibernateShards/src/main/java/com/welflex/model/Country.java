package com.welflex.model;

public enum Country {
  INDIA (0),
  USA(1),
  ITALY(2);
  
  private int code;
  
  Country(int code) {
    this.code = code;
  }

  public int toInt() {
    return code;
  }

   // the valueOfMethod
   public  static Country fromInt(int value) {    
       switch(value) {
           case 0: return INDIA;
           case 1: return USA;
           case 2: return ITALY;
           default:
                  throw new RuntimeException("Unknown type");
       }
  }
 
  public String toString() {
    switch(this) {
      case INDIA:
          return "India";
      case USA:
          return "Usa";
      case ITALY:
          return "Italy";
      default:
        return "Unknown";
    }
  }

}
