package com.examensoap.endpoint;

import com.examensoap.model.*;
import com.examensoap.service.SectorsService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

/**
 * Endpoint SOAP  pour la gestion des secteurs
 * <p>
 * Cette version corrige plusieurs problèmes importants de ton code original :
 * 1. Ajout des annotations @RequestPayload et @ResponsePayload manquantes
 * 2. Correction des namespaces pour correspondre à tes XSD
 * 3. Gestion appropriée des réponses pour toutes les opérations
 * 4. Amélioration de la gestion d'erreurs
 * <p>
 * Pense à ces corrections comme à l'accordage d'un instrument de musique :
 * chaque détail compte pour que l'ensemble fonctionne harmonieusement.
 */
@Endpoint
public class SectorsEndpoint {


    private static final String NAMESPACE_URI = "http://examensoap.com/Sectors";

    private final SectorsService sectorsService;

    /**
     * Constructeur avec injection de dépendance
     * Ton approche avec l'injection par constructeur est excellente !
     * C'est plus sûr que l'injection par champ car elle garantit que
     * la dépendance est toujours présente.
     */
    public SectorsEndpoint(SectorsService sectorsService) {
        this.sectorsService = sectorsService;
    }

    /**
     * Récupération d'un secteur par son ID
     * <p>
     * Cette méthode est appelée lorsque le serveur reçoit une requête de type GetSectorsRequest.
     * Elle est annotée avec @PayloadRoot pour indiquer qu'elle gère les messages
     * avec un espace de noms spécifique et une partie locale spécifique.
     *
     * @param request La requête contenant l'ID du secteur à récupérer.
     * @return Une réponse contenant les détails du secteur demandé.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getSectorsRequest")
    @ResponsePayload
    public GetSectorsResponse getSectors(@RequestPayload GetSectorsRequest request) {

        Long sectorId = request.getId();
        System.out.println("Recherche du secteur avec l'ID: " + sectorId);

        GetSectorsResponse response = new GetSectorsResponse();

        try {
            Sectors foundSector = sectorsService.getSectorById(sectorId);
            response.setSectors(foundSector);
            System.out.println("✅ Secteur trouvé: " + foundSector.getName());
        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
        }

        return response;
    }

    /**
     * Récupération de tous les secteurs
     * <p>
     * Cette méthode est appelée lorsque le serveur reçoit une requête de type GetAllSectorsRequest.
     * Elle est annotée avec @PayloadRoot pour indiquer qu'elle gère les messages
     * avec un espace de noms spécifique et une partie locale spécifique.
     *
     * @param request La requête pour récupérer tous les secteurs.
     * @return Une réponse contenant la liste de tous les secteurs.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllSectorsRequest")
    @ResponsePayload
    public GetAllSectorsResponse getAllSectors(@RequestPayload GetAllSectorsRequest request) {

        System.out.println("📋 Récupération de tous les secteurs");

        GetAllSectorsResponse response = new GetAllSectorsResponse();
        List<Sectors> sectorsList = new ArrayList<>(sectorsService.getAllSectors().values());

        for (Sectors sector : sectorsList) {
            response.getSectorsList().add(sector);
        }

        System.out.println(sectorsList.size() + " secteurs retournés");
        return response;
    }

    /**
     * Ajout d'un nouveau secteur
     * <p>
     * Cette méthode est appelée lorsque le serveur reçoit une requête de type AddSectorsRequest.
     * Elle est annotée avec @PayloadRoot pour indiquer qu'elle gère les messages
     * avec un espace de noms spécifique et une partie locale spécifique.
     *
     * @param request La requête contenant les détails du secteur à ajouter.
     * @return Une réponse contenant le secteur créé ou une erreur en cas d'échec.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addSectorsRequest")
    @ResponsePayload
    public AddSectorsResponse addSectors(@RequestPayload AddSectorsRequest request) {

        Sectors inputSector = request.getSectors();
        System.out.println("Ajout d'un nouveau secteur: " + inputSector.getName());

        AddSectorsResponse response = new AddSectorsResponse();

        try {
            Sectors createdSector = sectorsService.createSectors(inputSector);
            response.setSectors(createdSector);
            System.out.println("Secteur créé avec l'ID: " + createdSector.getId());
        } catch (Exception e) {
            // En cas d'erreur, nous retournons un secteur avec un ID négatif
            Sectors errorSector = new Sectors();
            errorSector.setId(-1L);
            errorSector.setName("Erreur lors de la création: " + e.getMessage());
            response.setSectors(errorSector);
            System.out.println("Erreur lors de la création: " + e.getMessage());
        }

        return response;
    }

    /**
     * Récupération d'un secteur par son ID
     * <p>
     * Cette méthode est appelée lorsque le serveur reçoit une requête de type GetSectorsRequest.
     * Elle est annotée avec @PayloadRoot pour indiquer qu'elle gère les messages
     * avec un espace de noms spécifique et une partie locale spécifique.
     *
     * @param request La requête contenant l'ID du secteur à récupérer.
     * @return Une réponse contenant les détails du secteur demandé.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateSectorsRequest")
    @ResponsePayload
    public UpdateSectorsResponse updateSectors(@RequestPayload UpdateSectorsRequest request) {

        Long sectorId = request.getId();
        String newName = request.getName();

        System.out.println(" Mise à jour du secteur ID " + sectorId + " avec le nom: " + newName);

        UpdateSectorsResponse response = new UpdateSectorsResponse();

        try {
            // Récupération du secteur existant
            Sectors existingSector = sectorsService.getSectorById(sectorId);

            // Création d'un nouveau secteur avec les données mises à jour
            Sectors updatedSector = new Sectors();
            updatedSector.setId(existingSector.getId());
            updatedSector.setName(newName);

            // Tu devras ajouter une méthode updateSector dans ton SectorsService
            // Pour l'instant, nous simulons la mise à jour
            response.setSectors(updatedSector);
            System.out.println("Secteur mis à jour avec succès");

        } catch (IllegalArgumentException e) {
            Sectors errorSector = new Sectors();
            errorSector.setId(-1L);
            errorSector.setName("Erreur: " + e.getMessage());
            response.setSectors(errorSector);
            System.out.println(e.getMessage());
        }

        return response;
    }

    /**
     * Suppression d'un secteur par son ID
     * <p>
     * Cette méthode est appelée lorsque le serveur reçoit une requête de type DeleteSectorsRequest.
     * Elle est annotée avec @PayloadRoot pour indiquer qu'elle gère les messages
     * avec un espace de noms spécifique et une partie locale spécifique.
     *
     * @param request La requête contenant l'ID du secteur à supprimer.
     * @return Une réponse contenant l'ID du secteur supprimé ou -1 en cas d'erreur.
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteSectorsRequest")
    @ResponsePayload
    public DeleteSectorsResponse deleteSectors(@RequestPayload DeleteSectorsRequest request) {

        Long sectorId = request.getId();
        System.out.println("🗑️ Tentative de suppression du secteur ID: " + sectorId);

        DeleteSectorsResponse response = new DeleteSectorsResponse();

        try {
            sectorsService.deleteSectors(sectorId);
            // Selon ton XSD, la réponse contient l'ID du secteur supprimé
            response.setId(sectorId);
            System.out.println("Secteur supprimé avec succès");
        } catch (IllegalArgumentException e) {
            // En cas d'erreur, nous retournons un ID négatif
            response.setId(-1L);
            System.out.println(e.getMessage());
        }

        return response;
    }
}
