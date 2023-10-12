package br.com.rafaeluz.taskmanager.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rafaeluz.taskmanager.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        /**
         * @Validar Rotas Authorization
         */
        var pathRequest = request.getServletPath();

        if (pathRequest.startsWith("/tasks/")) {

            /**
             * - Pegar usuario e Senha
             * - Validar usuario e Senha
             * - Autenticar usuário
             */
            var authorization = request.getHeader("Authorization");
            var user_password = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64.getDecoder().decode(user_password);

            var authString = new String(authDecode);

            String[] authArray = authString.split(":");
            String username = authArray[0];
            String password = authArray[1];

            var user = this.userRepository.findByUserName(username);

            if (user == null) {

                response.sendError(401);

            } else {

                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {

                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);

                } else {

                    response.sendError(401);
                }
            }

        } else {

            filterChain.doFilter(request, response);

        }

    }

}
