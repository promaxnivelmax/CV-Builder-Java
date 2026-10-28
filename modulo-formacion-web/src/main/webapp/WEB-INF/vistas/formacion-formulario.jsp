<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, co.edu.sena.cvbuilder.modelo.Formacion" %>
<%-- Vista: formulario HTML para registrar o editar un estudio (se envía por POST al servlet) --%>
<%!
    private static final String[] NIVELES = {"Bachiller", "Técnico", "Tecnólogo", "Profesional",
        "Especialización", "Maestría", "Curso"};

    private String html(Object valor) {
        if (valor == null) { return ""; }
        return valor.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    Formacion f = (Formacion) request.getAttribute("formacion");
    boolean esNuevo = f.getIdFormacion() == 0;
    @SuppressWarnings("unchecked")
    List<String> errores = (List<String>) request.getAttribute("errores");
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title><%= esNuevo ? "Nuevo estudio" : "Editar estudio" %> - CV Builder</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<%@ include file="encabezado.jspf" %>
<main class="contenedor angosto">
  <h1><%= esNuevo ? "Agregar estudio" : "Editar estudio" %></h1>
  <p class="ayuda">Los campos marcados con * son obligatorios.</p>
  <% if (errores != null && !errores.isEmpty()) { %>
    <div class="alerta error"><ul><% for (String e : errores) { %><li><%= html(e) %></li><% } %></ul></div>
  <% } %>
  <form class="tarjeta" method="post" action="${pageContext.request.contextPath}/formacion">
    <input type="hidden" name="id" value="<%= esNuevo ? "" : f.getIdFormacion() %>">
    <label for="titulo">Título obtenido o programa *</label>
    <input id="titulo" name="titulo" value="<%= html(f.getTitulo()) %>" placeholder="Ej.: Técnico en Sistemas">
    <label for="institucion">Institución *</label>
    <input id="institucion" name="institucion" value="<%= html(f.getInstitucion()) %>" placeholder="Ej.: SENA">
    <label for="nivel">Nivel *</label>
    <select id="nivel" name="nivel">
      <option value="">Seleccione…</option>
      <% for (String nivel : NIVELES) { %>
        <option <%= nivel.equals(f.getNivel()) ? "selected" : "" %>><%= nivel %></option>
      <% } %>
    </select>
    <div class="fila">
      <div><label for="fechaInicio">Fecha de inicio *</label>
        <input type="date" id="fechaInicio" name="fechaInicio" value="<%= html(f.getFechaInicio()) %>"></div>
      <div><label for="fechaFin">Fecha de finalización</label>
        <input type="date" id="fechaFin" name="fechaFin" value="<%= html(f.getFechaFin()) %>"></div>
    </div>
    <label class="check"><input type="checkbox" name="enCurso" <%= f.isEnCurso() ? "checked" : "" %>> Actualmente estudio aquí</label>
    <div class="botones">
      <a class="btn sec" href="${pageContext.request.contextPath}/formacion">Cancelar</a>
      <button class="btn" type="submit">Guardar</button>
    </div>
  </form>
</main>
</body>
</html>
