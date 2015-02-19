<%@ include file="/jsp/common/common.jsp"%>
<c:if test="${totalNoOfPages != 0}">
	<ul class="pagination">
		<c:choose>
			<c:when test="${totalNoOfPages eq 1 }">
			</c:when>
			<c:when test="${currentPage eq 0 }">
				<c:if test="${(totalNoOfPages ) lt 10}">
					<c:set var="end" value="${totalNoOfPages -1}"/>
				</c:if>
				<c:if test="${(totalNoOfPages ) gt 9}">
					<c:set var="end" value="9"/>
				</c:if>
				<c:forEach var='i' begin="0" end="${end}" step="1">
					<c:if test="${ i == currentPage}">
						<c:set var="pagerClass" value="active"/>
					</c:if>
					<c:if test="${ i != currentPage}">
						<c:set var="pagerClass" value=""/>
					</c:if>
					<li class="${pagerClass }">
						<a href="#" onclick="goToPageNo(this);" data-page-no="${i }">${i +1 }</a>
					</li>
				</c:forEach>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage + 1 }">next</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${totalNoOfPages -1 }">»</a></li>
			</c:when>
			<c:when test="${currentPage eq (totalNoOfPages-1) }">
				<c:if test="${(totalNoOfPages ) lt 10}">
					<c:set var="start" value="0"/>
				</c:if>
				<c:if test="${(totalNoOfPages ) gt 9}">
					<c:set var="start" value="${totalNoOfPages-10}"/>
				</c:if>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="0">«</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage -1 }">prev</a></li>
				<c:forEach var='i' begin="${start }" end="${totalNoOfPages - 1}" step="1">
					<c:if test="${ i == currentPage}">
						<c:set var="pagerClass" value="active"/>
					</c:if>
					<c:if test="${ i != currentPage}">
						<c:set var="pagerClass" value=""/>
					</c:if>
					<li class="${pagerClass }">
						<a href="#" onclick="goToPageNo(this);" data-page-no="${i }">${i +1 }</a>
					</li>
				</c:forEach>
			</c:when>
			<c:when test="${currentPage lt 5 }">
				<c:if test="${(totalNoOfPages ) lt 10}">
					<c:set var="end" value="${totalNoOfPages -1}"/>
				</c:if>
				<c:if test="${(totalNoOfPages ) gt 9}">
					<c:set var="end" value="9"/>
				</c:if>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="0">«</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage -1 }">prev</a></li>
				<c:forEach var='i' begin="0" end="${end }" step="1">
					<c:if test="${ i == currentPage}">
						<c:set var="pagerClass" value="active"/>
					</c:if>
					<c:if test="${ i != currentPage}">
						<c:set var="pagerClass" value=""/>
					</c:if>
					<li class="${pagerClass }">
						<a href="#" onclick="goToPageNo(this);" data-page-no="${i }">${i +1 }</a>
					</li>
				</c:forEach>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage + 1 }">next</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${totalNoOfPages -1 }">»</a></li>
			</c:when>
			<c:when test="${currentPage gt (totalNoOfPages-6) }">
				<c:if test="${(totalNoOfPages ) lt 10}">
					<c:set var="start" value="0"/>
				</c:if>
				<c:if test="${(totalNoOfPages ) gt 9}">
					<c:set var="start" value="${totalNoOfPages-10}"/>
				</c:if>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="0">«</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage -1 }">prev</a></li>
				<c:forEach var='i' begin="${start }" end="${totalNoOfPages-1 }" step="1">
					<c:if test="${ i == currentPage}">
						<c:set var="pagerClass" value="active"/>
					</c:if>
					<c:if test="${ i != currentPage}">
						<c:set var="pagerClass" value=""/>
					</c:if>
					<li class="${pagerClass }">
						<a href="#" onclick="goToPageNo(this);" data-page-no="${i }">${i +1 }</a>
					</li>
				</c:forEach>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage + 1 }">next</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${totalNoOfPages -1 }">»</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="0">«</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage -1 }">prev</a></li>
				<c:forEach var='i' begin="${currentPage -5 }" end="${currentPage + 5 }" step="1">
					<c:if test="${ i == currentPage}">
						<c:set var="pagerClass" value="active"/>
					</c:if>
					<c:if test="${ i != currentPage}">
						<c:set var="pagerClass" value=""/>
					</c:if>
					<li class="${pagerClass }">
						<a href="#" onclick="goToPageNo(this);" data-page-no="${i }">${i +1 }</a>
					</li>
				</c:forEach>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${currentPage + 1 }">next</a></li>
				<li><a href="#" onclick="goToPageNo(this);" data-page-no="${totalNoOfPages -1 }">»</a></li>
			</c:otherwise>
		</c:choose>
	</ul>
	<script type="text/javascript">
		function goToPageNo(element){
			var pageNo = jQuery(element).data("page-no");
			jQuery("input[name='paginator.currentPageNo']").val(pageNo).closest('form').submit();
		}
	</script>
</c:if>