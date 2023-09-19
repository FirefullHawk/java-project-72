@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.utils.NamedRoutes
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
  <div class="container-lg mt-5">
    <h1>Сайты</h1>
    <table class="table table-bordered table-hover mt-3">
      <thead>
      <tr>
        <th class="col-1">ID</th>
        <th>Имя</th>
        <th class="col-2">Последняя проверка</th>
        <th class="col-1">Код ответа</th>
      </tr>
      </thead>
      <tbody>
      @for(var url : page.getUrls())
        <tr>
          <td class="col-1">${url.getId()}</td>
          <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
          <td class="col-2">
            @if (page.getUrlChecks().stream().anyMatch(urlCheck -> urlCheck.getUrlId() == url.getId()))
            ${page.getUrlChecks().stream().filter(urlCheck -> urlCheck.getUrlId() == url.getId()).findFirst().orElse(null).getCreatedAt().toString().substring(0,16)}
            @endif
          </td>
          <td class="col-1">
            @if (page.getUrlChecks().stream().anyMatch(urlCheck -> urlCheck.getUrlId() == url.getId()))
            ${page.getUrlChecks().stream().filter(urlCheck -> urlCheck.getUrlId() == url.getId()).findFirst().orElse(null).getStatusCode()}
            @endif
          </td>
        </tr>
      @endfor
      </tbody>
    </table>

    <nav aria-label="Page navigation">
      <ul class="pagination justify-content-center mt-5">
        <li class="page-item ${page.getConditionPagingBack()}">
          <a class="page-link" href="${NamedRoutes.urlsPath()}?page=${page.getPageNumber() > 1? page.getPageNumber() - 1 : 1}">Previous</a>
        </li>
        <li class="page-item active">
          <a class="page-link" href="${NamedRoutes.urlsPath()}?page=${page.getPageNumber()}">${page.getPageNumber()}</a>
        </li>
        <li class="page-item ${page.getConditionPagingNext()}">
          <a class="page-link" href="${NamedRoutes.urlsPath()}?page=${page.getPageNumber() + 1}">Next</a>
        </li>
      </ul>
    </nav>
  </div>
`,
)