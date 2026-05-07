package br.com.evelyn.calorias.controller;

import br.com.evelyn.calorias.model.Usuario;
import br.com.evelyn.calorias.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody Usuario usuario) {
        return usuarioService.salvarUsuario(usuario);
    }

    @GetMapping("/usuarios")
    @ResponseStatus(HttpStatus.OK)
    public List<Usuario> litarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/usuarios/{usuarioId}")
    public Usuario buscarPorId(@PathVariable Long usuarioId) {
        return usuarioService.buscarPorId(usuarioId);
    }

    @DeleteMapping("/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long usuarioId) {
        usuarioService.excluir(usuarioId);
    }

    @PutMapping("/usuarios")
    @ResponseStatus(HttpStatus.OK)
    public Usuario atualizar(@RequestBody Usuario usuario) {
        return usuarioService.atualizar(usuario);
    }

}
