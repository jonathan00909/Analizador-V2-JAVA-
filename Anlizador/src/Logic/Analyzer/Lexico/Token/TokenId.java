/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logic.Analyzer.Lexico.Token;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author josue
 */
public @AllArgsConstructor @Data class TokenId {
    public TokenId(TokenId d){
        this(d.id,d.description);
    }
    public TokenId(){
        this.id = -1;
        this.description = " ";
    }
    
    private int id;
    private String description;
}
