package umg.programacionIII.Repo_Spring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umg.programacionIII.model.Usuario;
import umg.programacionIII.service.UsuarioService;
import umg.programacionIII.estructuras.lista.Lista;
import umg.programacionIII.estructuras.lista.Opcional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public Lista<Usuario> obtenerTodos() {
        return usuarioService.findAll();
    }

    @PostMapping
    public Usuario guardar(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        Opcional<Usuario> usuario = usuarioService.findById(id);
        return usuario.estaPresente() ?
                ResponseEntity.ok(usuario.obtener()) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}