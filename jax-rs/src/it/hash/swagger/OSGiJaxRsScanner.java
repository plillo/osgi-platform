package it.hash.swagger;

import io.swagger.config.SwaggerConfig;
import io.swagger.jaxrs.config.DefaultJaxrsScanner;
import io.swagger.models.Swagger;

public class OSGiJaxRsScanner extends DefaultJaxrsScanner implements SwaggerConfig {

  private final SwaggerConfiguration swaggerConfiguration;

  public OSGiJaxRsScanner( SwaggerConfiguration swaggerConfiguration ) {
    this.swaggerConfiguration = swaggerConfiguration;
  }

  @Override
  public Swagger configure( Swagger swagger ) {
    swagger.setInfo( swaggerConfiguration.getInfo() );
    swagger.setBasePath( swaggerConfiguration.getBasePath() );
    swagger.setHost( swaggerConfiguration.getHost() );
 
    return swagger;
  }

  @Override
  public String getFilterClass() {
    return swaggerConfiguration.getFilterClass();
  }

}
