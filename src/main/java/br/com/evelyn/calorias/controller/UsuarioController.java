package br.com.evelyn.calorias.controller;

import br.com.evelyn.calorias.dto.UsuarioCadastroDTO;
import br.com.evelyn.calorias.dto.UsuarioExibicaoDTO;
import br.com.evelyn.calorias.model.Usuario;
import br.com.evelyn.calorias.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioExibicaoDTO salvar(@RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {

        return usuarioService.salvarUsuario(usuarioCadastroDTO);
    }

    @GetMapping("/usuarios")
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioExibicaoDTO> litarTodos() {

        return usuarioService.listarTodos();
    }

    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<UsuarioExibicaoDTO> buscarPorId(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(usuarioService.buscarPorId(usuarioId));
        } catch (Exception e) {
          return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/usuarios/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long usuarioId) {
        usuarioService.excluir(usuarioId);
    }

    @PutMapping("/usuarios")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioExibicaoDTO atualizar(@RequestBody Usuario usuario) {
        return new UsuarioExibicaoDTO(usuarioService.atualizar(usuario));
    }

}
