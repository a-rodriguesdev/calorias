package br.com.evelyn.calorias.service;

import br.com.evelyn.calorias.model.Usuario;
import br.com.evelyn.calorias.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        }
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public void excluir(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            usuarioRepository.delete(usuarioOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        }
    }

    public Usuario atualizar(Usuario usuario) {
        if (usuario.getUsuarioId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioId e obrigatorio no corpo para atualizar");
        }
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(usuario.getUsuarioId());
        if (usuarioOptional.isPresent()) {
            return usuarioRepository.save(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado");
        }
    }
}
