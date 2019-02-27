package naakcii.by.api;

import com.vaadin.flow.server.VaadinServlet;
import naakcii.by.api.admin.MainView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import naakcii.by.api.util.parser.DataParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;


@SpringBootApplication
@EnableScheduling
public class APIApplication extends SpringBootServletInitializer implements CommandLineRunner {

//    private static final String FILE_WITH_CHAIN_PRODUCTS = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "Test_chain_products.xlsx";
//
//	@Autowired
//	private DataParser dataParser;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(APIApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }


    @Bean
    public ServletRegistrationBean frontendServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean<>(new VaadinServlet() {
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                if (!serveStaticOrWebJarRequest(req, resp)) {
                    resp.sendError(404);
                }
            }
        }, "/frontend/*");
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO Auto-generated method stub
//        dataParser.parseBasicData();
//        dataParser.parseChainProducts(FILE_WITH_CHAIN_PRODUCTS, "almi");
        System.out.println("Hello world");
    }

}
