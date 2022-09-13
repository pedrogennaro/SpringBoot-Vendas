package io.github.pedrogennaro.service.impl;

import io.github.pedrogennaro.api.dto.InformacoesPedidoDTO;
import io.github.pedrogennaro.api.dto.UsuarioDTO;
import io.github.pedrogennaro.domain.entity.Usuario;
import io.github.pedrogennaro.domain.repository.UsuarioRepository;
import io.github.pedrogennaro.exception.SenhaInvalidaException;
import io.github.pedrogennaro.exception.UsuarioJaExisteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        String[] roles = usuario.isAdmin()
                ? new String[] {"ADMIN", "USER"}
                : new String[] {"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }

    public UserDetails autenticar(Usuario usuario){
        UserDetails usuarioBD = loadUserByUsername(usuario.getLogin());
        boolean validacaoSenha = encoder.matches(usuario.getSenha(), usuarioBD.getPassword());
        if(validacaoSenha){
            return usuarioBD;
        }
        throw new SenhaInvalidaException();
    }

    @Transactional
    public UsuarioDTO save(Usuario usuario){
        Optional<Usuario> userExist = usuarioRepository.findByLogin(usuario.getLogin());
        if(!userExist.isPresent()){
            String passwordCripto = passwordEncoder.encode(usuario.getSenha());
            usuario.setSenha(passwordCripto);
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            return converterUsuario(usuarioSalvo);
        }
        throw new UsuarioJaExisteException();
    }

    private UsuarioDTO converterUsuario(Usuario usuario){
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .login(usuario.getLogin())
                .admin(usuario.isAdmin())
                .build();
    }

}
