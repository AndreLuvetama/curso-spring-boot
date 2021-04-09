package br.com.forum.controller;


import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.forum.controller.form.AtualizacaoTopicoForm;
import br.com.forum.dto.DetalhesDoTopicoDto;
import br.com.forum.dto.TopicoDto;

import br.com.forum.modelo.Topico;
import br.com.forum.repository.CursoRepository;
import br.com.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository; 
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso){
		if (nomeCurso == null)
		{
			List<Topico> topicos =  topicoRepository.findAll();
			return TopicoDto.convert(topicos);
		} else {
			List<Topico> topicos =  topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.convert(topicos);
		}		
				
	}
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico  = form.converter(cursoRepository);
		topicoRepository.save(topico);
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	/** Busca por ID
	@GetMapping("/{id}") //@PathVariable informa que o variavel vem como parte da url e não como interrogação
	public TopicoDto detalhar(@PathVariable Long id) {
		Topico topico = topicoRepository.getOne(id); // faz a busca por id
		return new TopicoDto(topico); // converte em DTo
		
	}**/
	
	@GetMapping("/{id}") //@PathVariable informa que o variavel vem como parte da url e não como interrogação
	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
		Topico topico = topicoRepository.getOne(id); // faz a busca por id
		return new DetalhesDoTopicoDto(topico); // converte em DTo
		
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form  ){
		Topico topico = form.atualizar(id, topicoRepository);
		
		return ResponseEntity.ok(new TopicoDto(topico));
		
	}
	
	

}
