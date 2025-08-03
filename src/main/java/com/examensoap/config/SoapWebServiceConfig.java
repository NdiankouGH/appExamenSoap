package com.examensoap.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Configuration du service web SOAP pour l'application.
 * <p>
 * Cette classe configure les composants nécessaires à l'exposition de services web SOAP
 * avec Spring Boot, notamment l'activation du support SOAP, l'enregistrement du servlet
 * de dispatch, et la déclaration des schémas XSD et WSDL.
 * <p>
 * - \@Configuration : indique à Spring que cette classe fournit des définitions de beans à gérer.
 * - \@EnableWs : active l'infrastructure SOAP de Spring Web Services.
 * <p>
 * Les schémas XSD sont utilisés pour valider les messages échangés et générer dynamiquement
 * les WSDL exposés par l'application.
 */
@EnableWs
@Configuration
public class SoapWebServiceConfig {

    /**
     * Configure et enregistre le MessageDispatcherServlet pour gérer les requêtes SOAP.
     *
     * @param applicationContext le contexte d'application Spring injecté automatiquement
     * @return un bean ServletRegistrationBean configuré pour le dispatcher SOAP
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> mesageDispacherServlet(ApplicationContext applicationContext) {

        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformSchemaLocations(true);
        servlet.setTransformWsdlLocations(true);

        ServletRegistrationBean<MessageDispatcherServlet> registrationBean =
                new ServletRegistrationBean<>(servlet, "/ws/*");
        registrationBean.setName("messageDispatcherServlet");
        return registrationBean;
    }

    //Configuration des schémas XSD pour le système de classes et secteurs

    /**
     * Définit le schéma XSD pour les secteurs.
     *
     * @return un bean XsdSchema basé sur le fichier sectors.xsd situé dans le dossier schemas du classpath
     */


    @Bean(name = "sectorsSchema")
    public XsdSchema sectorsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/Sectors.xsd"));
    }

    /**
     * Définit le schéma XSD pour les classes.
     *
     * @return un bean XsdSchema nommé "classes" basé sur le fichier Classes.xsd situé dans le dossier xsd du classpath
     */

    @Bean(name = "classesSchema")
    public XsdSchema classesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/Classes.xsd"));
    }

    /**
     * Définit le bean WSDL pour les secteurs.
     * <p>
     * Ce bean expose le schéma XSD des secteurs via un WSDL généré dynamiquement,
     * accessible à l'URL /ws/Sectors.wsdl.
     *
     * @param sectorsSchema le schéma XSD des secteurs injecté automatiquement par Spring
     * @return une instance de DefaultWsdl11Definition configurée pour les secteurs
     */
    @Bean(name = "sectorsWsdl")
    public DefaultWsdl11Definition sectorsWsdl11Definition(XsdSchema sectorsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("SectorsPort");
        wsdl11Definition.setLocationUri("/ws/");
        wsdl11Definition.setTargetNamespace("http://examensoap.com/Sectors");
        wsdl11Definition.setSchema(sectorsSchema);
        return wsdl11Definition;

    }


    /**
     * Définit le bean WSDL pour les classes.
     * <p>
     * Ce bean expose le schéma XSD des classes via un WSDL généré dynamiquement,
     * accessible à l'URL /ws/classes.wsdl.
     *
     * @param classesSchema le schéma XSD des classes injecté automatiquement par Spring
     * @return une instance de DefaultWsdl11Definition configurée pour les classes
     */
    @Bean(name = "classesWsdl")
    public DefaultWsdl11Definition classesWsdl11Definition(XsdSchema classesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ClassesPort");
        wsdl11Definition.setLocationUri("/ws/");
        wsdl11Definition.setTargetNamespace("http://examensoap.com/Classes");
        wsdl11Definition.setSchema(classesSchema);
        return wsdl11Definition;
    }
}
