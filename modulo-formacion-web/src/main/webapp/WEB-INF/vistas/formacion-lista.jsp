<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, co.edu.sena.cvbuilder.modelo.Formacion" %>
<%-- Vista: lista de estudios del perfil (JSP con scriptlets, expresiones y EL) --%>
<%!
    /** Escapa caracteres especiales para mostrarlos de forma segura en HTML. */
    private String html(String texto) {
        if (texto == null) { return ""; }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Formación académica - CV Builder</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<%@ include file="encabezado.jspf" %>
<main class="contenedor">
  <div class="titulo">
    <h1>Formación académica</h1>
    <a class="btn" href="${pageContext.request.contextPath}/formacion?accion=nuevo">+ Agregar estudio</a>
  </div>
  <% String mensaje = (String) request.getAttribute("mensaje");
     if (mensaje != null && !mensaje.isBlank()) { %>
    <div class="alerta ok"><%= html(mensaje) %></div>
  <% } %>
  <%
    @SuppressWarnings("unchecked")
    List<Formacion> estudios = (List<Formacion>) request.getAttribute("estudios");
  %>
  <p class="ayuda">Registros encontrados: <b><%= estudios.size() %></b></p>
  <table>
    <thead>
      <tr><th>Título</th><th>Institución</th><th>Nivel</th><th>Periodo</th><th>Acciones</th></tr>
    </thead>
    <tbody>
    <% if (estudios.isEmpty()) { %>
      <tr><td colspan="5" class="vacio">Aún no has registrado estudios.</td></tr>
    <% } %>
    <% for (Formacion f : estudios) { %>
      <tr>
        <td><%= html(f.getTitulo()) %></td>
        <td><%= html(f.getInstitucion()) %></td>
        <td><%= html(f.getNivel()) %></td>
        <td><%= f.getFechaInicio() %> – <%= f.isEnCurso() ? "En curso" : f.getFechaFin() %></td>
        <td class="acciones">
          <a class="btn sec" href="${pageContext.request.contextPath}/formacion?accion=editar&id=<%= f.getIdFormacion() %>">Editar</a>
          <form method="post" action="${pageContext.request.contextPath}/formacion"
                onsubmit="return confirm('¿Eliminar este estudio?');">
            <input type="hidden" name="accion" value="eliminar">
            <input type="hidden" name="id" value="<%= f.getIdFormacion() %>">
            <button class="btn peligro" type="submit">Eliminar</button>
          </form>
        </td>
      </tr>
    <% } %>
    </tbody>
  </table>
</main>
</body>
</html>
