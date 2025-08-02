package com.examensoap.endpoint;

import com.examensoap.model.*;
import com.examensoap.service.ClassesService;
import com.examensoap.service.SectorsService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassesEndpoint gère les requêtes SOAP liées aux classes.
 * Il permet de récupérer, créer et supprimer des classes.
 */
@Endpoint
public class ClassesEndpoint {


    private static final String NAMESPACE_URI = "http://examensoap.com/Classes";

    private final ClassesService classesService;
    private final SectorsService sectorsService;


    public ClassesEndpoint(ClassesService classesService, SectorsService sectorsService) {
        this.classesService = classesService;
        this.sectorsService = sectorsService;
    }

    /**
     * Récupération d'une classe par son ID.
     * Cette méthode traite la requête SOAP `getClassesRequest'.
     * Elle récupère l'ID de la classe depuis la requête, utilise le service
     * `ClassesService` pour trouver la classe correspondante, et retourne
     * la réponse avec les détails de la classe.
     * * En cas d'erreur (classe non trouvée), elle crée une instance de
     *
     * @param request
     * @return
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getClassesRequest")
    @ResponsePayload
    public GetClassesResponse getClasses(@RequestPayload GetClassesRequest request) {

        Long classId = request.getId();
        System.out.println("Recherche de la classe avec l'ID: " + classId);

        GetClassesResponse response = new GetClassesResponse();

        try {
            Classes foundClass = classesService.getClassById(classId);
            response.setClasses(foundClass);
            System.out.println("Classe trouvée: " + foundClass.getClassName() );
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        return response;
    }

    /**
     * Récupération de toutes les classes.
     * Cette méthode traite la requête SOAP `getAllClassesRequest`.
     * Elle utilise le service `ClassesService` pour obtenir la liste de toutes les classes
     * et retourne une réponse contenant cette liste.
     *
     * @param request La requête pour récupérer toutes les classes (peut être vide)
     * @return Une réponse contenant la liste de toutes les classes
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllClassesRequest")
    @ResponsePayload
    public GetAllClassesResponse getAllClasses(@RequestPayload GetAllClassesRequest request) {

        System.out.println("Récupération de toutes les classes");

        GetAllClassesResponse response = new GetAllClassesResponse();
        List<Classes> listClasses = new ArrayList<>(classesService.listAllClasses().values());

        for (Classes classes : listClasses) {
            response.getClassLitst().add(classes); // Note: "ClassLitst" à cause du XSD
        }

        System.out.println(listClasses.size() + " classes retournées");
        return response;
    }


    /**
     * Traite la requête SOAP `createClassesRequest` pour créer une nouvelle classe.
     * <p>
     * Cette méthode :
     * - Récupère les informations de la classe à créer depuis la requête (nom, description, secteur).
     * - Valide l'existence du secteur via le service `SectorsService`.
     * - Crée une nouvelle instance de `Classes` et l'enregistre via le service `ClassesService`.
     * - Retourne la classe créée dans la réponse, ou rien si une erreur survient.
     *
     * @param request l'objet `CreateClassesRequest` contenant les données de la classe à créer
     * @return un objet `CreateClassesResponse` contenant la classe créée ou vide en cas d'erreur
     */

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createClassesRequest")
    @ResponsePayload
    public CreateClassesResponse createClasses(@RequestPayload CreateClassesRequest request) {

        String className = request.getClassName();
        String description = request.getDescription();
        Long requestedSector = request.getSectors();

        System.out.println(" Création d'une nouvelle classe: " + className);

        CreateClassesResponse response = new CreateClassesResponse();

        try {

           // Sectors validatedSector = sectorsService.getSectorById(requestedSector.getId());

            Classes newClass = new Classes();
            newClass.setClassName(className);
            newClass.setDescription(description);
           // newClass.setSectors(validatedSector.getId());

            Classes createdClass = classesService.createClass(newClass);
            response.setClasses(createdClass);

            System.out.println("Classe créée avec l'ID: " + createdClass.getId());

        } catch (IllegalArgumentException e) {

            System.out.println("Erreur lors de la création: " + e.getMessage());
        }

        return response;
    }


    /**
     * Suppression d'une classe par ID.
     * <p>
     * Cette méthode traite la requête SOAP `deleteClassesRequest` pour supprimer une classe
     * identifiée par son ID. Elle vérifie d'abord l'existence de la classe, puis effectue la suppression.
     * En cas de succès, un message de confirmation est retourné dans la réponse.
     * En cas d'échec (classe inexistante ou autre erreur métier), un message d'erreur explicite est renvoyé.
     *
     * @param request l'objet `DeleteClassesRequest` contenant l'ID de la classe à supprimer
     * @return un objet `DeleteClassesResponse` avec un message de succès ou d'erreur
     */
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteClassesRequest")
    @ResponsePayload
    public DeleteClassesResponse deleteClasses(@RequestPayload DeleteClassesRequest request) {

        Long classId = request.getId();
        System.out.println("🗑️ Tentative de suppression de la classe ID: " + classId);

        DeleteClassesResponse response = new DeleteClassesResponse();

        try {
            // Récupération des informations de la classe avant suppression
            Classes classToDelete = classesService.getClassById(classId);
            String className = classToDelete.getClassName();
            Long sectorId = classToDelete.getSectors();
            // String sectorName = classToDelete;

            // Suppression de la classe
            classesService.deleteClass(classId);

            String successMessage = "Classe '" + className + " supprimée avec succès";
            response.setMessage(successMessage);
            System.out.println(successMessage);

        } catch (IllegalArgumentException e) {
            String errorMessage = "Erreur lors de la suppression: " + e.getMessage();
            response.setMessage(errorMessage);
            System.out.println(errorMessage);
        }

        return response;
    }
}
