package com.examensoap.endpoint;

import com.examensoap.dto.ClassesDto;
import com.examensoap.exception.ServiceException;
import com.examensoap.model.*;
import com.examensoap.service.impl.ClassesService;
import com.examensoap.service.impl.SectorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

/**
 * ClassesEndpoint gère les requêtes SOAP liées aux classes.
 * Il permet de récupérer, créer et supprimer des classes.
 */
@Endpoint
public class ClassesEndpoint {


    private static final String NAMESPACE_URI = "http://examensoap.com/Classes";
    private static final Logger logger = LoggerFactory.getLogger(ClassesEndpoint.class);

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
        logger.info("Recherche de la classe avec l'ID: {}", classId);

        GetClassesResponse response = new GetClassesResponse();

        try {
            ClassesDto foundClassDto = classesService.getClasseById(classId);
            Classes foundClass = convertToSoapClass(foundClassDto);
            response.setClasses(foundClass);
            logger.info("Classe trouvée: {}", foundClass.getClassName());
        } catch (ServiceException e) {
            logger.error("Erreur lors de la recherche de la classe ID {}: {}", classId, e.getMessage());
            // Retourner une réponse vide ou avec message d'erreur selon votre XSD
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la recherche de la classe ID {}: {}", classId, e.getMessage());
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
        logger.info("Récupération de toutes les classes");

        GetAllClassesResponse response = new GetAllClassesResponse();

        try {
            List<ClassesDto> listClasses = classesService.getAllClasses();

            for (ClassesDto classDto : listClasses) {
                response.getClassLitst().add(convertToSoapClass(classDto));
            }

            logger.info("{} classes retournées", listClasses.size());
        } catch (ServiceException e) {
            logger.error("Erreur lors de la récupération des classes: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la récupération des classes: {}", e.getMessage());
        }

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
        String className = request.getClassName().getClassName();
        logger.info("Création d'une nouvelle classe: {}", className);

        CreateClassesResponse response = new CreateClassesResponse();

        try {

            // Validation de l'existence du secteur
            Long sectorId = request.getClassName().getSectors();
            sectorsService.getSectorById(sectorId);

            // Création du DTO
            ClassesDto newClassDto = new ClassesDto();
            newClassDto.setClassName(className.trim());
            newClassDto.setDescription(request.getClassName().getDescription());
            newClassDto.setSectorId(sectorId);

            // Création via le service (avec gestion transactionnelle)
            ClassesDto createdClassDto = classesService.createClasse(newClassDto);

            // Conversion pour la réponse SOAP
            Classes createdClass = convertToSoapClass(createdClassDto);
            response.setClasses(createdClass);

            logger.info("Classe créée avec l'ID: {}", createdClassDto.getId());

        } catch (ServiceException e) {
            logger.error("Erreur métier lors de la création de la classe '{}': {}", className, e.getMessage());
            // Selon votre XSD, vous pourriez retourner un objet avec un ID négatif ou un message d'erreur
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création de la classe '{}': {}", className, e.getMessage());
        }

        return response;
    }

    /**
     * Mise à jour d'une classe par ID.
     * <p>
     * Cette méthode traite la requête SOAP `updateClassesRequest` pour mettre à jour une classe
     * identifiée par son ID. Elle récupère les nouvelles données de la classe depuis la requête,
     * utilise le service `ClassesService` pour effectuer la mise à jour, et retourne la classe mise à jour.
     * En cas d'erreur (classe non trouvée ou autre erreur métier), un message d'erreur est enregistré.
     *
     * @param request l'objet `UpdateClassesRequest` contenant l'ID de la classe et les nouvelles données
     * @return un objet `UpdateClassesResponse` contenant la classe mise à jour ou vide en cas d'erreur

     @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateClassesRequest")
     @ResponsePayload public UpdateClassesResponse updateClasses(@RequestPayload  request) {
     Long classId = request.getId();
     logger.info("Mise à jour de la classe ID: {}", classId);

     UpdateClassesResponse response = new UpdateClassesResponse();

     try {
     // Création du DTO avec les nouvelles données
     ClassesDto updateDto = new ClassesDto();
     updateDto.setClassName(request.getClassName());
     updateDto.setDescription(request.getDescription());
     updateDto.setSectorId(request.getSectorId());

     // Mise à jour via le service
     ClassesDto updatedClassDto = classesService.updateClasse(classId, updateDto);

     // Conversion pour la réponse SOAP
     Classes updatedClass = convertToSoapClass(updatedClassDto);
     response.setClasses(updatedClass);

     logger.info("Classe mise à jour avec succès: {}", updatedClass.getClassName());

     } catch (ServiceException e) {
     logger.error("Erreur lors de la mise à jour de la classe ID {}: {}", classId, e.getMessage());
     } catch (Exception e) {
     logger.error("Erreur inattendue lors de la mise à jour de la classe ID {}: {}", classId, e.getMessage());
     }

     return response;
     }

     */

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
        logger.info("Tentative de suppression de la classe ID: {}", classId);

        DeleteClassesResponse response = new DeleteClassesResponse();

        try {
            // Récupération des informations avant suppression pour le log
            ClassesDto classToDelete = classesService.getClasseById(classId);

            // Suppression via le service (avec gestion transactionnelle)
            classesService.deleteClasse(classId);

            response.setMessage("Classe '" + classToDelete.getClassName() + "' (ID: " + classId + ") supprimée avec succès.");
            logger.info("Classe supprimée: {} (ID: {})", classToDelete.getClassName(), classId);

        } catch (ServiceException e) {
            String errorMessage = "Erreur lors de la suppression de la classe ID " + classId + ": " + e.getMessage();
            response.setMessage(errorMessage);
            logger.error(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Erreur inattendue lors de la suppression de la classe ID " + classId + ": " + e.getMessage();
            response.setMessage(errorMessage);
            logger.error(errorMessage);
        }

        return response;
    }

    /**
     * Convertit un DTO de classe en un objet SOAP Classes.
     *
     * @param dto l'objet DTO à convertir
     * @return l'objet Classes correspondant
     */
    private Classes convertToSoapClass(ClassesDto dto) {
        Classes classes = new Classes();
        classes.setId(dto.getId());
        classes.setClassName(dto.getClassName());
        classes.setDescription(dto.getDescription());
        classes.setSectors(dto.getSectorId() != null ? dto.getSectorId() : 1L);
        return classes;
    }
}
