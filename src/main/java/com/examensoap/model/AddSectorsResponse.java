//
// Ce fichier a été généré par Eclipse Implementation of JAXB, v3.0.0 
// Voir https://eclipse-ee4j.github.io/jaxb-ri 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2025.08.02 à 04:47:29 PM GMT 
//


package com.examensoap.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sectors" type="{http://examensoap.com/Sectors}Sectors"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sectors"
})
@XmlRootElement(name = "addSectorsResponse")
public class AddSectorsResponse {

    @XmlElement(required = true)
    protected Sectors sectors;

    /**
     * Obtient la valeur de la propriété sectors.
     * 
     * @return
     *     possible object is
     *     {@link Sectors }
     *     
     */
    public Sectors getSectors() {
        return sectors;
    }

    /**
     * Définit la valeur de la propriété sectors.
     * 
     * @param value
     *     allowed object is
     *     {@link Sectors }
     *     
     */
    public void setSectors(Sectors value) {
        this.sectors = value;
    }

}
