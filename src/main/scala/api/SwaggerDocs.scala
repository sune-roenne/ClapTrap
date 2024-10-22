package api

import com.github.swagger.pekko.SwaggerHttpService
import com.github.swagger.pekko.model.Info

object SwaggerDocs extends SwaggerHttpService{
  override def apiClasses: Set[Class[_]] = Set(AccessApi.getClass)

  override val host = "localhost:8081"
  override val info : Info = Info(
    description = "ClapTrap handles issuance of access tokens",
    title= "ClapTrap API",
    version = "1.0")
  super.apiDocsPath


}
