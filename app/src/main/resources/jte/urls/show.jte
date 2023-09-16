@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.utils.NamedRoutes
@param UrlPage page

@template.layout.page(
content = @`
    <div class="container-lg mt-5">
      <h1>Сайт ${page.getUrl().getName()}</h1>

      <table class="table table-bordered table-hover mt-3">
        <tbody>
          <tr>
            <td>ID</td>
            <td>${page.getUrl().getId()}</td>
          </tr>
          <tr>
            <td>Имя</td>
            <td>${page.getUrl().getName()}</td>
          </tr>
          <tr>
            <td>Дата создания</td>
            <td>${page.getUrl().getCreatedAt().toString().substring(0, 16)}</td>
          </tr>
        </tbody>
      </table>

      <h2 class="mt-5">Проверки</h2>
      <form method="post" action="${NamedRoutes.checkPath(page.getUrl().getId())}">
        <button type="submit" class="btn btn-primary">Запустить проверку</button>
      </form>

      <table class="table table-bordered table-hover mt-3">
        <thead>
          <tr>
            <th class="col-1">ID</th>
            <th class="col-1">Код ответа</th>
            <th>title</th>
            <th>h1</th>
            <th>description</th>
            <th class="col-2">Дата проверки</th>
          </tr>
        </thead>
          @for(var checkedUrl : page.getUrlCheck())
          <tbody>
            <tr>
              <td class="col-1">${checkedUrl.getId()}</td>
              <td class="col-1">${checkedUrl.getStatusCode()}</td>
              <td>${checkedUrl.getTitle()}</td>
              <td>${checkedUrl.getH1()}</td>
              <td>${checkedUrl.getDescription()}</td>
              <td class="col-2">${checkedUrl.getCreatedAt().toString().substring(0, 16)}</td>
            </tr>
          </tbody>
          @endfor
      </table>

      <nav aria-label="Page navigation">
        <ul class="pagination justify-content-center mt-5">
          <li class="page-item disabled">
            <a class="page-link" href="${NamedRoutes.urlPath(page.getUrl().getId())}?page=${page.getPageNumber() > 1? page.getPageNumber() - 1 : 1}">Previous</a>
          </li>
          <li class="page-item active">
            <a class="page-link" href="${NamedRoutes.urlPath(page.getUrl().getId())}?page=${page.getPageNumber()}">${page.getPageNumber()}</a>
          </li>
          <li class="page-item disabled">
            <a class="page-link" href="${NamedRoutes.urlPath(page.getUrl().getId())}?page=${page.getPageNumber() + 1}">Next</a>
          </li>
        </ul>
      </nav>
    </div>
`,
)
