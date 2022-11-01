/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.porfolio.lucas.Controller;

import com.porfolio.lucas.Dto.dtoProyecto;
import com.porfolio.lucas.Entity.Proyecto;
import com.porfolio.lucas.Security.Controller.Mensaje;
import com.porfolio.lucas.Service.SProyecto;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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

@RestController
@RequestMapping("/proyecto")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "https://myporfoliolucas.web.app")
public class CProyecto {
    @Autowired
    SProyecto sProyecto;
    
    @GetMapping("/lista")
    public ResponseEntity<List<Proyecto>> list(){
        List<Proyecto> list = sProyecto.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    
    @GetMapping("/detail/{id}")
    public ResponseEntity<Proyecto> getById(@PathVariable("id") int id){
        if(!sProyecto.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Proyecto proyecto = sProyecto.getOne(id).get();
        return new ResponseEntity(proyecto, HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoProyecto dtoproyecto){
        if(StringUtils.isBlank(dtoproyecto.getNombreP()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        if(sProyecto.existsByNombreP(dtoproyecto.getNombreP()))
            return new ResponseEntity(new Mensaje("Ese proyecto ya existe"), HttpStatus.BAD_REQUEST);
        
        Proyecto proyecto = new Proyecto(dtoproyecto.getNombreP(), dtoproyecto.getDescripcionP(), dtoproyecto.getImgP());
        sProyecto.save(proyecto);
        
        return new ResponseEntity(new Mensaje("Proyecto agregado"), HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody dtoProyecto dtoproyecto){
        //Validamos si existe el ID
        if(!sProyecto.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe"),HttpStatus.BAD_REQUEST);
        //Compara el nombre de la experiencia   
        if(sProyecto.existsByNombreP(dtoproyecto.getNombreP())&& sProyecto.getByNombreP(dtoproyecto.getNombreP()).get().getId() !=id)
            return new ResponseEntity(new Mensaje("Ese proyecto ya existe"), HttpStatus.BAD_REQUEST);
        //El campo no puede estar vacio
        if(StringUtils.isBlank(dtoproyecto.getNombreP()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);
        
        Proyecto proyecto = sProyecto.getOne(id).get();
        proyecto.setNombreP(dtoproyecto.getNombreP());
        proyecto.setDescripcionP(dtoproyecto.getDescripcionP());
        proyecto.setImgP(dtoproyecto.getImgP());
        
        sProyecto.save(proyecto);
        return new ResponseEntity(new Mensaje("Proyecto actualizada"), HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){
        //Validamos si existe el ID
        if(!sProyecto.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe"), HttpStatus.BAD_REQUEST);
        
        sProyecto.delete(id);
        
        return new ResponseEntity(new Mensaje("Proyecto eliminada"), HttpStatus.OK);
    }
}
