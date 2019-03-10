package naakcii.by.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan
public class APIApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(APIApplication.class, args);
    }


//    @Bean
//    public ServletRegistrationBean frontendServletBean() {
//        ServletRegistrationBean bean = new ServletRegistrationBean<>(new VaadinServlet() {
//            @Override
//            protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                if (!serveStaticOrWebJarRequest(req, resp)) {
//                    resp.sendError(404);
//                }
//            }
//        }, "/frontend/*");
//        bean.setLoadOnStartup(1);
//        return bean;
//    }
}
