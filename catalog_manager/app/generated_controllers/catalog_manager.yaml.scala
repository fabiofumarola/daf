
import play.api.mvc.{Action,Controller}

import play.api.data.validation.Constraint

import play.api.i18n.MessagesApi

import play.api.inject.{ApplicationLifecycle,ConfigurationProvider}

import de.zalando.play.controllers._

import PlayBodyParsing._

import PlayValidations._

import scala.util._

import javax.inject._

import de.zalando.play.controllers.PlayBodyParsing._
import it.gov.daf.catalogmanager.listeners.IngestionListenerImpl
import it.gov.daf.catalogmanager.service.{CkanRegistry,ServiceRegistry}
import play.api.libs.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import org.pac4j.play.store.PlaySessionStore
import play.api.Configuration
import it.gov.daf.catalogmanager.service.VocServiceRegistry
import akka.actor.ActorSystem
import it.gov.daf.catalogmanager.Utils
import it.gov.daf.play.security.CredentialsHandler
import it.gov.daf.play.security.CredentialsHandler.{BasicCredentials,JwtCredentials,UnauthorizedUser}
import play.api.libs.ws.WSClient

/**
 * This controller is re-generated after each change in the specification.
 * Please only place your hand-written code between appropriate comments in the body of the controller.
 */

package catalog_manager.yaml {
    // ----- Start of unmanaged code area for package Catalog_managerYaml
    
    // ----- End of unmanaged code area for package Catalog_managerYaml
    class Catalog_managerYaml @Inject() (
        // ----- Start of unmanaged code area for injections Catalog_managerYaml

    ingestionListener: IngestionListenerImpl,
    val configuration: Configuration,
    val playSessionStore: PlaySessionStore,
        // ----- End of unmanaged code area for injections Catalog_managerYaml
        val messagesApi: MessagesApi,
        lifecycle: ApplicationLifecycle,
        config: ConfigurationProvider
    ) extends Catalog_managerYamlBase {
        // ----- Start of unmanaged code area for constructor Catalog_managerYaml

    val GENERIC_ERROR = Error("An Error occurred", None, None)
    //        Authentication(configuration, playSessionStore)

        // ----- End of unmanaged code area for constructor Catalog_managerYaml
        val autocompletedummy = autocompletedummyAction { (autocompRes: AutocompRes) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.autocompletedummy
            NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.autocompletedummy
        }
        val searchdataset = searchdatasetAction { input: (MetadataCat, MetadataCat, ResourceSize, ResourceSize) =>
            val (q, sort, rows, start) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.searchdataset
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))
      val queryResult = fUsername.flatMap(u => CkanRegistry.ckanService.searchDatasets(input, Some(u)))

      queryResult.flatMap {
        case JsSuccess(dataset, _) => Searchdataset200(dataset)
        case e: JsError => Searchdataset401(Error(Utils.getMessageFromJsError(e), None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.searchdataset
        }
        val getckanorganizationbyid = getckanorganizationbyidAction { (org_id: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.getckanorganizationbyid
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val orgFuture = fUsername.flatMap(u => CkanRegistry.ckanService.getOrganization(org_id, Some(u)))

      orgFuture.flatMap {
        case JsSuccess(organization: Organization, _) => Getckanorganizationbyid200(organization)
        case e: JsError => Getckanorganizationbyid401(Error(Utils.getMessageFromJsError(e), None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.getckanorganizationbyid
        }
        val getckandatasetList = getckandatasetListAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.getckandatasetList
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))
      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.getDatasets(Some(u)))

      fResult.flatMap {
        case s: JsArray => GetckandatasetList200(s.as[Seq[String]])
        case _ => GetckandatasetList401(Error("An Error occurred", None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.getckandatasetList
        }
        val voc_subthemesgetall = voc_subthemesgetallAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_subthemesgetall
            val subthemeList: Seq[VocKeyValueSubtheme] = VocServiceRegistry.vocRepository.listSubthemeAll()
      Voc_subthemesgetall200(subthemeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_subthemesgetall
        }
        val datasetcatalogs = datasetcatalogsAction { input: (MetadataRequired, Dataset_catalogsGetLimit) =>
            val (page, limit) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.datasetcatalogs
            val pageIng: Option[Int] = page
      val limitIng: Option[Int] = limit
      val catalogs = ServiceRegistry.catalogService.listCatalogs(page, limit)

      catalogs match {
        case Seq() => Datasetcatalogs401("No data")
        case _ => Datasetcatalogs200(catalogs)
      }
      // Datasetcatalogs200(catalogs)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.datasetcatalogs
        }
        val voc_subthemesgetbyid = voc_subthemesgetbyidAction { (themeid: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_subthemesgetbyid
            val subthemeList: Seq[KeyValue] = VocServiceRegistry.vocRepository.listSubtheme(themeid)
      Voc_subthemesgetbyid200(subthemeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_subthemesgetbyid
        }
        val voc_dcat2dafsubtheme = voc_dcat2dafsubthemeAction { input: (String, String) =>
            val (themeid, subthemeid) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcat2dafsubtheme
            val themeList: Seq[VocKeyValueSubtheme] = VocServiceRegistry.vocRepository.dcat2DafSubtheme(input._1, input._2)
      Voc_dcat2dafsubtheme200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcat2dafsubtheme
        }
        val standardsuri = standardsuriAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.standardsuri
            // Pagination wrong refactor login to db query
      val catalogs = ServiceRegistry.catalogService.listCatalogs(Some(1), Some(500))
      val uris: Seq[String] = catalogs.filter(x => x.operational.is_std)
        .map(_.operational.logical_uri).map(_.toString)
      val stdUris: Seq[StdUris] = uris.map(x => StdUris(Some(x), Some(x)))
      Standardsuri200(Seq(StdUris(Some("ale"), Some("test"))))
      // NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.standardsuri
        }
        val autocompletedataset = autocompletedatasetAction { input: (MetadataCat, ResourceSize) =>
            val (q, limit) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.autocompletedataset
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))
      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.autocompleteDatasets(input, Some(u)))

      fResult.flatMap {
        case JsSuccess(response, _) => Autocompletedataset200(response)
        case e: JsError => Autocompletedataset401(Error(Utils.getMessageFromJsError(e), None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.autocompletedataset
        }
        val createdatasetcatalog = createdatasetcatalogAction { (catalog: MetaCatalog) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.createdatasetcatalog
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val fResult = fUsername.map(u => ServiceRegistry.catalogService.createCatalog(catalog, Some(u)))

      fResult.foreach {
        case created: Success if !created.message.toLowerCase.equals("error") =>
          val logicalUri = created.message.get
        //   ingestionListener.addDirListener(catalog, logicalUri)
      }

      Createdatasetcatalog200(fResult)
      //NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.createdatasetcatalog
        }
        val test = testAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.test
            NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.test
        }
        val verifycredentials = verifycredentialsAction { (credentials: Credentials) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.verifycredentials
            CkanRegistry.ckanService.verifyCredentials(credentials) match {
        case true => Verifycredentials200(Success("Success", Some("User verified")))
        case _ => Verifycredentials401(Error("Wrong Username or Password", None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.verifycredentials
        }
        val voc_dcatthemegetall = voc_dcatthemegetallAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcatthemegetall
            val themeList: Seq[KeyValue] = VocServiceRegistry.vocRepository.listDcatThemeAll()
      Voc_dcatthemegetall200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcatthemegetall
        }
        val createckandataset = createckandatasetAction { (dataset: Dataset) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.createckandataset
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val jsonv: JsValue = ResponseWrites.DatasetWrites.writes(dataset)

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.createDataset(jsonv, Some(u)))

      fResult.flatMap {
        case "true" => Createckandataset200(Success("Success", Some("dataset created")))
        case e => Createckandataset401(Error(e, None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.createckandataset
        }
        val getckandatasetListWithRes = getckandatasetListWithResAction { input: (ResourceSize, ResourceSize) =>
            val (limit, offset) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.getckandatasetListWithRes
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.getDatasetsWithRes(input, Some(u)))

      fResult.flatMap {
        case JsSuccess(dataset, path) => GetckandatasetListWithRes200(dataset)
        case e: JsError => GetckandatasetListWithRes401(Error(Utils.getMessageFromJsError(e), None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.getckandatasetListWithRes
        }
        val getckanuserorganizationList = getckanuserorganizationListAction { (username: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.getckanuserorganizationList
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.getUserOrganizations(username, Some(u)))

      fResult flatMap {
        case JsSuccess(orgs, _) => GetckanuserorganizationList200(orgs)
        case e: JsError => GetckanuserorganizationList401(Error(Utils.getMessageFromJsError(e), None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.getckanuserorganizationList
        }
        val voc_themesgetall = voc_themesgetallAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_themesgetall
            val themeList: Seq[KeyValue] = VocServiceRegistry.vocRepository.listThemeAll()
      Voc_themesgetall200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_themesgetall
        }
        val voc_dcatsubthemesgetall = voc_dcatsubthemesgetallAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcatsubthemesgetall
            val themeList: Seq[VocKeyValueSubtheme] = VocServiceRegistry.vocRepository.listSubthemeAll()
      Voc_dcatsubthemesgetall200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcatsubthemesgetall
        }
        val voc_daf2dcatsubtheme = voc_daf2dcatsubthemeAction { input: (String, String) =>
            val (themeid, subthemeid) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcatsubtheme
            val subthemeList: Seq[VocKeyValueSubtheme] = VocServiceRegistry.vocRepository.daf2dcatSubtheme(input._1, input._2)
      Voc_daf2dcatsubtheme200(subthemeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcatsubtheme
        }
        val createckanorganization = createckanorganizationAction { (organization: Organization) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.createckanorganization
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val jsonv: JsValue = ResponseWrites.OrganizationWrites.writes(organization)

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.createOrganization(jsonv, Some(u)))

      fResult.flatMap {
        case "true" => Createckanorganization200(Success("Success", Some("organization created")))
        case e => Createckanorganization401(Error(e, None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.createckanorganization
        }
        val updateckanorganization = updateckanorganizationAction { input: (String, Organization) =>
            val (org_id, organization) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.updateckanorganization
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))
      val jsonv: JsValue = ResponseWrites.OrganizationWrites.writes(organization)

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.updateOrganization(org_id, jsonv, Some(u)))

      fResult.flatMap {
        case "true" => Updateckanorganization200(Success("Success", Some("organization updated")))
        case e => Updateckanorganization401(Error(e, None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.updateckanorganization
        }
        val getckanuser = getckanuserAction { (username: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.getckanuser
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val fResult = fUsername.map(u => CkanRegistry.ckanService.getMongoUser(username, Some(u)))

      fResult.flatMap {
        case JsSuccess(user, path) => Getckanuser200(user)
        case e: JsError => Getckanuser401(Error("error, no user with that name", None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.getckanuser
        }
        val createckanuser = createckanuserAction { (user: User) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.createckanuser
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))
      val jsonv: JsValue = ResponseWrites.UserWrites.writes(user)

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.createUser(jsonv, Some(u)))

      fResult.flatMap {
        case "true" => Createckanuser200(Success("Success", Some("user created")))
        case e => Createckanuser401(Error(e, None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.createckanuser
        }
        val getckandatasetbyid = getckandatasetbyidAction { (dataset_id: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.getckandatasetbyid
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.testDataset(dataset_id, Some(u)))

      fResult.flatMap {
        case JsSuccess(dataset, _) => Getckandatasetbyid200(dataset)
        case e: JsError => Getckandatasetbyid401(Error(Utils.getMessageFromJsError(e), None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.getckandatasetbyid
        }
        val voc_dcat2Daftheme = voc_dcat2DafthemeAction { (themeid: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcat2Daftheme
            val themeList: Seq[KeyValue] = VocServiceRegistry.vocRepository.dcat2DafTheme(themeid)
      Voc_dcat2Daftheme200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcat2Daftheme
        }
        val patchckanorganization = patchckanorganizationAction { input: (String, Organization) =>
            val (org_id, organization) = input
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.patchckanorganization
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))
      val jsonv: JsValue = ResponseWrites.OrganizationWrites.writes(organization)

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.patchOrganization(org_id, jsonv, Some(u)))

      fResult.flatMap {
        case "true" => Patchckanorganization200(Success("Success", Some("organization patched")))
        case e => Patchckanorganization401(Error(e, None, None))
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.patchckanorganization
        }
        val datasetcatalogbyid = datasetcatalogbyidAction { (catalog_id: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.datasetcatalogbyid
            val logical_uri = new java.net.URI(catalog_id)
      val catalog = ServiceRegistry.catalogService.catalog(logical_uri.toString)
      println("*******")
      println(logical_uri.toString)
      println(catalog.toString)
      /*
            val resutl  = catalog match {
                case MetaCatalog(None,None,None) => Datasetcatalogbyid401("Error no data with that logical_uri")
                case  _ =>  Datasetcatalogbyid200(catalog)
            }
            resutl
            */

      catalog match {
        case Some(c) => Datasetcatalogbyid200(c)
        case None => Datasetcatalogbyid401("Error")
      }

      //Datasetcatalogbyid200(catalog.get)

      //NotImplementedYet
      //println("ale")
      //println(MetaCatalog(None,None,None))
      //Datasetcatalogbyid200(MetaCatalog(None,None,None))

      //Datasetcatalogbyid200(catalog.toString)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.datasetcatalogbyid
        }
        val voc_daf2dcattheme = voc_daf2dcatthemeAction { (themeid: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcattheme
            NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcattheme
        }
        val voc_dcatsubthemesgetbyid = voc_dcatsubthemesgetbyidAction { (themeid: String) =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcatsubthemesgetbyid
            val themeList: Seq[KeyValue] = VocServiceRegistry.vocRepository.listDcatSubtheme(themeid)
      Voc_dcatsubthemesgetbyid200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcatsubthemesgetbyid
        }
        val getckanorganizationList = getckanorganizationListAction {  _ =>  
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.getckanorganizationList
            val authenticator = CredentialsHandler(playSessionStore, configuration)
      val fUsername = Future.fromTry(authenticator.extractUsername(currentRequest))

      val fResult = fUsername.flatMap(u => CkanRegistry.ckanService.getOrganizations(Some(u)))

      fResult.flatMap{
        case s: JsArray => GetckanorganizationList200(s.as[Seq[String]])
        case _ => GetckanorganizationList401(GENERIC_ERROR)
      }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.getckanorganizationList
        }
    
     // Dead code for absent methodCatalog_managerYaml.createIPAuser
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.createIPAuser
            ApiClientIPA.createUser(user) flatMap {
                case Right(success) => CreateIPAuser200(success)
                case Left(err) => CreateIPAuser500(err)
            }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.createIPAuser
     */

    
     // Dead code for absent methodCatalog_managerYaml.voc_dcatap2dafsubtheme
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcatap2dafsubtheme
            val themeList: Seq[VocKeyValueSubtheme] = VocServiceRegistry.vocRepository.dcatapit2DafSubtheme(input._1, input._2)
            Voc_dcatap2dafsubtheme200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcatap2dafsubtheme
     */

    
     // Dead code for absent methodCatalog_managerYaml.voc_dcatapitthemegetall
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcatapitthemegetall
            NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcatapitthemegetall
     */

    
     // Dead code for absent methodCatalog_managerYaml.voc_daf2dcataptheme
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcataptheme
            val subthemeList: Seq[KeyValue] = VocServiceRegistry.vocRepository.daf2dcatapitTheme(themeid)
            Voc_daf2dcataptheme200(subthemeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcataptheme
     */

    
     // Dead code for absent methodCatalog_managerYaml.voc_dcatap2Daftheme
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_dcatap2Daftheme
            val themeList: Seq[KeyValue] = VocServiceRegistry.vocRepository.dcatapit2DafTheme(themeid)
            Voc_dcatap2Daftheme200(themeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_dcatap2Daftheme
     */

    
     // Dead code for absent methodCatalog_managerYaml.registrationconfirm
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.registrationconfirm
            RegistrationService.createUser(token) flatMap {
                case Right(success) => Registrationconfirm200(success)
                case Left(err) => Registrationconfirm500(err)
            }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.registrationconfirm
     */

    
     // Dead code for absent methodCatalog_managerYaml.tempo
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.tempo
            NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.tempo
     */

    
     // Dead code for absent methodCatalog_managerYaml.addDataset
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.addDataset
            NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.addDataset
     */

    
     // Dead code for absent methodCatalog_managerYaml.registrationrequest
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.registrationrequest
            val reg = RegistrationService.requestRegistration(user) flatMap {
                case Right(mailService) => mailService.sendMail()
                case Left(msg) => Future {Left(msg)}
            }

            reg flatMap {
                case Right(msg) => Registrationrequest200(Success(Some("Success"), Some(msg)))
                case Left(msg) => Registrationrequest500(Error(None, Option(msg), None))
            }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.registrationrequest
     */

    
     // Dead code for absent methodCatalog_managerYaml.voc_daf2dcatapsubtheme
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcatapsubtheme
            val subthemeList: Seq[VocKeyValueSubtheme] = VocServiceRegistry.vocRepository.daf2dcatapitSubtheme(input._1, input._2)
            Voc_daf2dcatapsubtheme200(subthemeList)
            // ----- End of unmanaged code area for action  Catalog_managerYaml.voc_daf2dcatapsubtheme
     */

    
     // Dead code for absent methodCatalog_managerYaml.showipauser
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.showipauser
            ApiClientIPA.showUser(uid) flatMap {
                case Right(success) => Showipauser200(success)
                case Left(err) => Showipauser500(err)
            }
            // ----- End of unmanaged code area for action  Catalog_managerYaml.showipauser
     */

    
     // Dead code for absent methodCatalog_managerYaml.ckandatasetbyid
     /*
                  // ----- Start of unmanaged code area for action  Catalog_managerYaml.ckandatasetbyid
                  val dataset: Future[Dataset] = ServiceRegistry.catalogService.getDataset(dataset_id)
                  Ckandatasetbyid200(dataset)
                  //NotImplementedYet
                  // ----- End of unmanaged code area for action  Catalog_managerYaml.ckandatasetbyid
     */

    
     // Dead code for absent methodCatalog_managerYaml.showIPAuser
     /*
            // ----- Start of unmanaged code area for action  Catalog_managerYaml.showIPAuser
            NotImplementedYet
            // ----- End of unmanaged code area for action  Catalog_managerYaml.showIPAuser
     */

    
    }
}
