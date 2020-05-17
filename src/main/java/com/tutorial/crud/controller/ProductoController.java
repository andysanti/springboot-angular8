package com.tutorial.crud.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tutorial.crud.dto.Mensaje;
import com.tutorial.crud.dto.ProductoDTO;
import com.tutorial.crud.entity.Producto;
import com.tutorial.crud.service.ProductoService;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductoController {
	
	@Autowired
	ProductoService productoService;
	
	@GetMapping("/lista")
	public ResponseEntity<List<Producto>> list(){
		
		List<Producto> lista= productoService.list();
		return new ResponseEntity(lista,HttpStatus.OK);
		
	}
	
	@GetMapping("/detail/{id}")
	public ResponseEntity<Producto> getById(@PathVariable("id") int id){
		
		if(!productoService.exitsById(id)) {
			return new ResponseEntity(new Mensaje("no existe producto"),HttpStatus.NOT_FOUND);
		}
		
		Producto producto= productoService.getOne(id).get();
		return new ResponseEntity(producto,HttpStatus.OK);
		
	}
	
	@GetMapping("/detailname/{nombre}")
	public ResponseEntity<Producto> getByNombre(@PathVariable("nombre") String nombre) {

		if (!productoService.exitsByNombre(nombre)) {
			return new ResponseEntity(new Mensaje("no existe producto"), HttpStatus.NOT_FOUND);
		}

		Producto producto = productoService.getByNombre(nombre).get();
		return new ResponseEntity(producto, HttpStatus.OK);

	}
	
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ProductoDTO productoDto){
		
		if(org.apache.commons.lang3.StringUtils.isBlank(productoDto.getNombre())) {
			return new ResponseEntity(new Mensaje("el nombre es obligatorio"),HttpStatus.BAD_REQUEST);
		}
		
		if(productoDto.getPrecio()==null || productoDto.getPrecio()<0) {
			return new ResponseEntity(new Mensaje("el precio debe ser mayor a cero"),HttpStatus.BAD_REQUEST);
		}
		if(productoService.exitsByNombre(productoDto.getNombre())) {
			return new ResponseEntity(new Mensaje("el nombre ya existe"),HttpStatus.BAD_REQUEST);
		}
		
		Producto producto = new Producto(productoDto.getNombre(),productoDto.getPrecio());
		
		productoService.save(producto);
		
		
		return new ResponseEntity(new Mensaje("producto creado exitosamente."),HttpStatus.OK);
		
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody ProductoDTO productoDto){
		
		
		if(!productoService.exitsById(id)) {
			return new ResponseEntity(new Mensaje("no existe producto"),HttpStatus.NOT_FOUND);
		}
		
		if(productoService.exitsByNombre(productoDto.getNombre()) && productoService.getByNombre(productoDto.getNombre()).get().getId()!=id) {
			return new ResponseEntity(new Mensaje("el nombre ya existe"),HttpStatus.BAD_REQUEST);
		}
		
		if( org.apache.commons.lang3.StringUtils.isBlank(productoDto.getNombre())) {
			return new ResponseEntity(new Mensaje("el nombre es obligatorio"),HttpStatus.BAD_REQUEST);
		}
		
		if(productoDto.getPrecio()<0) {
			return new ResponseEntity(new Mensaje("el precio debe ser mayor a cero"),HttpStatus.BAD_REQUEST);
		}
		
		
		Producto producto = productoService.getOne(id).get();
		producto.setNombre(productoDto.getNombre());
		producto.setPrecio(productoDto.getPrecio());
		
		productoService.save(producto);
		
		
		return new ResponseEntity(new Mensaje("producto actualizado exitosamente."),HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") int id ) {
		
		if(!productoService.exitsById(id)) {
			return new ResponseEntity(new Mensaje("no existe."),HttpStatus.NOT_FOUND);
		}
		productoService.delete(id);
		
		return new ResponseEntity(new Mensaje("producto eliminado exitosamente."),HttpStatus.OK);
		
	}
	

}
