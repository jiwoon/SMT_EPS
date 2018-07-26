package com.jimi.smt.eps.display.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgramItemVisitExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProgramItemVisitExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andLineseatIsNull() {
            addCriterion("lineseat is null");
            return (Criteria) this;
        }

        public Criteria andLineseatIsNotNull() {
            addCriterion("lineseat is not null");
            return (Criteria) this;
        }

        public Criteria andLineseatEqualTo(String value) {
            addCriterion("lineseat =", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotEqualTo(String value) {
            addCriterion("lineseat <>", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatGreaterThan(String value) {
            addCriterion("lineseat >", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatGreaterThanOrEqualTo(String value) {
            addCriterion("lineseat >=", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatLessThan(String value) {
            addCriterion("lineseat <", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatLessThanOrEqualTo(String value) {
            addCriterion("lineseat <=", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatLike(String value) {
            addCriterion("lineseat like", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotLike(String value) {
            addCriterion("lineseat not like", value, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatIn(List<String> values) {
            addCriterion("lineseat in", values, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotIn(List<String> values) {
            addCriterion("lineseat not in", values, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatBetween(String value1, String value2) {
            addCriterion("lineseat between", value1, value2, "lineseat");
            return (Criteria) this;
        }

        public Criteria andLineseatNotBetween(String value1, String value2) {
            addCriterion("lineseat not between", value1, value2, "lineseat");
            return (Criteria) this;
        }

        public Criteria andMaterialNoIsNull() {
            addCriterion("material_no is null");
            return (Criteria) this;
        }

        public Criteria andMaterialNoIsNotNull() {
            addCriterion("material_no is not null");
            return (Criteria) this;
        }

        public Criteria andMaterialNoEqualTo(String value) {
            addCriterion("material_no =", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotEqualTo(String value) {
            addCriterion("material_no <>", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoGreaterThan(String value) {
            addCriterion("material_no >", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoGreaterThanOrEqualTo(String value) {
            addCriterion("material_no >=", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoLessThan(String value) {
            addCriterion("material_no <", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoLessThanOrEqualTo(String value) {
            addCriterion("material_no <=", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoLike(String value) {
            addCriterion("material_no like", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotLike(String value) {
            addCriterion("material_no not like", value, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoIn(List<String> values) {
            addCriterion("material_no in", values, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotIn(List<String> values) {
            addCriterion("material_no not in", values, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoBetween(String value1, String value2) {
            addCriterion("material_no between", value1, value2, "materialNo");
            return (Criteria) this;
        }

        public Criteria andMaterialNoNotBetween(String value1, String value2) {
            addCriterion("material_no not between", value1, value2, "materialNo");
            return (Criteria) this;
        }

        public Criteria andProgramIdIsNull() {
            addCriterion("program_id is null");
            return (Criteria) this;
        }

        public Criteria andProgramIdIsNotNull() {
            addCriterion("program_id is not null");
            return (Criteria) this;
        }

        public Criteria andProgramIdEqualTo(String value) {
            addCriterion("program_id =", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotEqualTo(String value) {
            addCriterion("program_id <>", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdGreaterThan(String value) {
            addCriterion("program_id >", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdGreaterThanOrEqualTo(String value) {
            addCriterion("program_id >=", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdLessThan(String value) {
            addCriterion("program_id <", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdLessThanOrEqualTo(String value) {
            addCriterion("program_id <=", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdLike(String value) {
            addCriterion("program_id like", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotLike(String value) {
            addCriterion("program_id not like", value, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdIn(List<String> values) {
            addCriterion("program_id in", values, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotIn(List<String> values) {
            addCriterion("program_id not in", values, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdBetween(String value1, String value2) {
            addCriterion("program_id between", value1, value2, "programId");
            return (Criteria) this;
        }

        public Criteria andProgramIdNotBetween(String value1, String value2) {
            addCriterion("program_id not between", value1, value2, "programId");
            return (Criteria) this;
        }

        public Criteria andScanLineseatIsNull() {
            addCriterion("scan_lineseat is null");
            return (Criteria) this;
        }

        public Criteria andScanLineseatIsNotNull() {
            addCriterion("scan_lineseat is not null");
            return (Criteria) this;
        }

        public Criteria andScanLineseatEqualTo(String value) {
            addCriterion("scan_lineseat =", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatNotEqualTo(String value) {
            addCriterion("scan_lineseat <>", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatGreaterThan(String value) {
            addCriterion("scan_lineseat >", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatGreaterThanOrEqualTo(String value) {
            addCriterion("scan_lineseat >=", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatLessThan(String value) {
            addCriterion("scan_lineseat <", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatLessThanOrEqualTo(String value) {
            addCriterion("scan_lineseat <=", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatLike(String value) {
            addCriterion("scan_lineseat like", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatNotLike(String value) {
            addCriterion("scan_lineseat not like", value, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatIn(List<String> values) {
            addCriterion("scan_lineseat in", values, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatNotIn(List<String> values) {
            addCriterion("scan_lineseat not in", values, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatBetween(String value1, String value2) {
            addCriterion("scan_lineseat between", value1, value2, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanLineseatNotBetween(String value1, String value2) {
            addCriterion("scan_lineseat not between", value1, value2, "scanLineseat");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoIsNull() {
            addCriterion("scan_material_no is null");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoIsNotNull() {
            addCriterion("scan_material_no is not null");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoEqualTo(String value) {
            addCriterion("scan_material_no =", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoNotEqualTo(String value) {
            addCriterion("scan_material_no <>", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoGreaterThan(String value) {
            addCriterion("scan_material_no >", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoGreaterThanOrEqualTo(String value) {
            addCriterion("scan_material_no >=", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoLessThan(String value) {
            addCriterion("scan_material_no <", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoLessThanOrEqualTo(String value) {
            addCriterion("scan_material_no <=", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoLike(String value) {
            addCriterion("scan_material_no like", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoNotLike(String value) {
            addCriterion("scan_material_no not like", value, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoIn(List<String> values) {
            addCriterion("scan_material_no in", values, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoNotIn(List<String> values) {
            addCriterion("scan_material_no not in", values, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoBetween(String value1, String value2) {
            addCriterion("scan_material_no between", value1, value2, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andScanMaterialNoNotBetween(String value1, String value2) {
            addCriterion("scan_material_no not between", value1, value2, "scanMaterialNo");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeIsNull() {
            addCriterion("last_operation_type is null");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeIsNotNull() {
            addCriterion("last_operation_type is not null");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeEqualTo(Integer value) {
            addCriterion("last_operation_type =", value, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeNotEqualTo(Integer value) {
            addCriterion("last_operation_type <>", value, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeGreaterThan(Integer value) {
            addCriterion("last_operation_type >", value, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("last_operation_type >=", value, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeLessThan(Integer value) {
            addCriterion("last_operation_type <", value, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeLessThanOrEqualTo(Integer value) {
            addCriterion("last_operation_type <=", value, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeIn(List<Integer> values) {
            addCriterion("last_operation_type in", values, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeNotIn(List<Integer> values) {
            addCriterion("last_operation_type not in", values, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeBetween(Integer value1, Integer value2) {
            addCriterion("last_operation_type between", value1, value2, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andLastOperationTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("last_operation_type not between", value1, value2, "lastOperationType");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultIsNull() {
            addCriterion("store_issue_result is null");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultIsNotNull() {
            addCriterion("store_issue_result is not null");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultEqualTo(Integer value) {
            addCriterion("store_issue_result =", value, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultNotEqualTo(Integer value) {
            addCriterion("store_issue_result <>", value, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultGreaterThan(Integer value) {
            addCriterion("store_issue_result >", value, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("store_issue_result >=", value, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultLessThan(Integer value) {
            addCriterion("store_issue_result <", value, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultLessThanOrEqualTo(Integer value) {
            addCriterion("store_issue_result <=", value, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultIn(List<Integer> values) {
            addCriterion("store_issue_result in", values, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultNotIn(List<Integer> values) {
            addCriterion("store_issue_result not in", values, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultBetween(Integer value1, Integer value2) {
            addCriterion("store_issue_result between", value1, value2, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueResultNotBetween(Integer value1, Integer value2) {
            addCriterion("store_issue_result not between", value1, value2, "storeIssueResult");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeIsNull() {
            addCriterion("store_issue_time is null");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeIsNotNull() {
            addCriterion("store_issue_time is not null");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeEqualTo(Date value) {
            addCriterion("store_issue_time =", value, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeNotEqualTo(Date value) {
            addCriterion("store_issue_time <>", value, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeGreaterThan(Date value) {
            addCriterion("store_issue_time >", value, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("store_issue_time >=", value, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeLessThan(Date value) {
            addCriterion("store_issue_time <", value, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeLessThanOrEqualTo(Date value) {
            addCriterion("store_issue_time <=", value, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeIn(List<Date> values) {
            addCriterion("store_issue_time in", values, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeNotIn(List<Date> values) {
            addCriterion("store_issue_time not in", values, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeBetween(Date value1, Date value2) {
            addCriterion("store_issue_time between", value1, value2, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andStoreIssueTimeNotBetween(Date value1, Date value2) {
            addCriterion("store_issue_time not between", value1, value2, "storeIssueTime");
            return (Criteria) this;
        }

        public Criteria andFeedResultIsNull() {
            addCriterion("feed_result is null");
            return (Criteria) this;
        }

        public Criteria andFeedResultIsNotNull() {
            addCriterion("feed_result is not null");
            return (Criteria) this;
        }

        public Criteria andFeedResultEqualTo(Integer value) {
            addCriterion("feed_result =", value, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultNotEqualTo(Integer value) {
            addCriterion("feed_result <>", value, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultGreaterThan(Integer value) {
            addCriterion("feed_result >", value, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("feed_result >=", value, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultLessThan(Integer value) {
            addCriterion("feed_result <", value, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultLessThanOrEqualTo(Integer value) {
            addCriterion("feed_result <=", value, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultIn(List<Integer> values) {
            addCriterion("feed_result in", values, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultNotIn(List<Integer> values) {
            addCriterion("feed_result not in", values, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultBetween(Integer value1, Integer value2) {
            addCriterion("feed_result between", value1, value2, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedResultNotBetween(Integer value1, Integer value2) {
            addCriterion("feed_result not between", value1, value2, "feedResult");
            return (Criteria) this;
        }

        public Criteria andFeedTimeIsNull() {
            addCriterion("feed_time is null");
            return (Criteria) this;
        }

        public Criteria andFeedTimeIsNotNull() {
            addCriterion("feed_time is not null");
            return (Criteria) this;
        }

        public Criteria andFeedTimeEqualTo(Date value) {
            addCriterion("feed_time =", value, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeNotEqualTo(Date value) {
            addCriterion("feed_time <>", value, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeGreaterThan(Date value) {
            addCriterion("feed_time >", value, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("feed_time >=", value, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeLessThan(Date value) {
            addCriterion("feed_time <", value, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeLessThanOrEqualTo(Date value) {
            addCriterion("feed_time <=", value, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeIn(List<Date> values) {
            addCriterion("feed_time in", values, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeNotIn(List<Date> values) {
            addCriterion("feed_time not in", values, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeBetween(Date value1, Date value2) {
            addCriterion("feed_time between", value1, value2, "feedTime");
            return (Criteria) this;
        }

        public Criteria andFeedTimeNotBetween(Date value1, Date value2) {
            addCriterion("feed_time not between", value1, value2, "feedTime");
            return (Criteria) this;
        }

        public Criteria andChangeResultIsNull() {
            addCriterion("change_result is null");
            return (Criteria) this;
        }

        public Criteria andChangeResultIsNotNull() {
            addCriterion("change_result is not null");
            return (Criteria) this;
        }

        public Criteria andChangeResultEqualTo(Integer value) {
            addCriterion("change_result =", value, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultNotEqualTo(Integer value) {
            addCriterion("change_result <>", value, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultGreaterThan(Integer value) {
            addCriterion("change_result >", value, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("change_result >=", value, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultLessThan(Integer value) {
            addCriterion("change_result <", value, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultLessThanOrEqualTo(Integer value) {
            addCriterion("change_result <=", value, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultIn(List<Integer> values) {
            addCriterion("change_result in", values, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultNotIn(List<Integer> values) {
            addCriterion("change_result not in", values, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultBetween(Integer value1, Integer value2) {
            addCriterion("change_result between", value1, value2, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeResultNotBetween(Integer value1, Integer value2) {
            addCriterion("change_result not between", value1, value2, "changeResult");
            return (Criteria) this;
        }

        public Criteria andChangeTimeIsNull() {
            addCriterion("change_time is null");
            return (Criteria) this;
        }

        public Criteria andChangeTimeIsNotNull() {
            addCriterion("change_time is not null");
            return (Criteria) this;
        }

        public Criteria andChangeTimeEqualTo(Date value) {
            addCriterion("change_time =", value, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeNotEqualTo(Date value) {
            addCriterion("change_time <>", value, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeGreaterThan(Date value) {
            addCriterion("change_time >", value, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("change_time >=", value, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeLessThan(Date value) {
            addCriterion("change_time <", value, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeLessThanOrEqualTo(Date value) {
            addCriterion("change_time <=", value, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeIn(List<Date> values) {
            addCriterion("change_time in", values, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeNotIn(List<Date> values) {
            addCriterion("change_time not in", values, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeBetween(Date value1, Date value2) {
            addCriterion("change_time between", value1, value2, "changeTime");
            return (Criteria) this;
        }

        public Criteria andChangeTimeNotBetween(Date value1, Date value2) {
            addCriterion("change_time not between", value1, value2, "changeTime");
            return (Criteria) this;
        }

        public Criteria andCheckResultIsNull() {
            addCriterion("check_result is null");
            return (Criteria) this;
        }

        public Criteria andCheckResultIsNotNull() {
            addCriterion("check_result is not null");
            return (Criteria) this;
        }

        public Criteria andCheckResultEqualTo(Integer value) {
            addCriterion("check_result =", value, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultNotEqualTo(Integer value) {
            addCriterion("check_result <>", value, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultGreaterThan(Integer value) {
            addCriterion("check_result >", value, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("check_result >=", value, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultLessThan(Integer value) {
            addCriterion("check_result <", value, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultLessThanOrEqualTo(Integer value) {
            addCriterion("check_result <=", value, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultIn(List<Integer> values) {
            addCriterion("check_result in", values, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultNotIn(List<Integer> values) {
            addCriterion("check_result not in", values, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultBetween(Integer value1, Integer value2) {
            addCriterion("check_result between", value1, value2, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckResultNotBetween(Integer value1, Integer value2) {
            addCriterion("check_result not between", value1, value2, "checkResult");
            return (Criteria) this;
        }

        public Criteria andCheckTimeIsNull() {
            addCriterion("check_time is null");
            return (Criteria) this;
        }

        public Criteria andCheckTimeIsNotNull() {
            addCriterion("check_time is not null");
            return (Criteria) this;
        }

        public Criteria andCheckTimeEqualTo(Date value) {
            addCriterion("check_time =", value, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeNotEqualTo(Date value) {
            addCriterion("check_time <>", value, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeGreaterThan(Date value) {
            addCriterion("check_time >", value, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("check_time >=", value, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeLessThan(Date value) {
            addCriterion("check_time <", value, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeLessThanOrEqualTo(Date value) {
            addCriterion("check_time <=", value, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeIn(List<Date> values) {
            addCriterion("check_time in", values, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeNotIn(List<Date> values) {
            addCriterion("check_time not in", values, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeBetween(Date value1, Date value2) {
            addCriterion("check_time between", value1, value2, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckTimeNotBetween(Date value1, Date value2) {
            addCriterion("check_time not between", value1, value2, "checkTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultIsNull() {
            addCriterion("check_all_result is null");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultIsNotNull() {
            addCriterion("check_all_result is not null");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultEqualTo(Integer value) {
            addCriterion("check_all_result =", value, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultNotEqualTo(Integer value) {
            addCriterion("check_all_result <>", value, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultGreaterThan(Integer value) {
            addCriterion("check_all_result >", value, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("check_all_result >=", value, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultLessThan(Integer value) {
            addCriterion("check_all_result <", value, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultLessThanOrEqualTo(Integer value) {
            addCriterion("check_all_result <=", value, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultIn(List<Integer> values) {
            addCriterion("check_all_result in", values, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultNotIn(List<Integer> values) {
            addCriterion("check_all_result not in", values, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultBetween(Integer value1, Integer value2) {
            addCriterion("check_all_result between", value1, value2, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllResultNotBetween(Integer value1, Integer value2) {
            addCriterion("check_all_result not between", value1, value2, "checkAllResult");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeIsNull() {
            addCriterion("check_all_time is null");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeIsNotNull() {
            addCriterion("check_all_time is not null");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeEqualTo(Date value) {
            addCriterion("check_all_time =", value, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeNotEqualTo(Date value) {
            addCriterion("check_all_time <>", value, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeGreaterThan(Date value) {
            addCriterion("check_all_time >", value, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("check_all_time >=", value, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeLessThan(Date value) {
            addCriterion("check_all_time <", value, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeLessThanOrEqualTo(Date value) {
            addCriterion("check_all_time <=", value, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeIn(List<Date> values) {
            addCriterion("check_all_time in", values, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeNotIn(List<Date> values) {
            addCriterion("check_all_time not in", values, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeBetween(Date value1, Date value2) {
            addCriterion("check_all_time between", value1, value2, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andCheckAllTimeNotBetween(Date value1, Date value2) {
            addCriterion("check_all_time not between", value1, value2, "checkAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultIsNull() {
            addCriterion("first_check_all_result is null");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultIsNotNull() {
            addCriterion("first_check_all_result is not null");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultEqualTo(Integer value) {
            addCriterion("first_check_all_result =", value, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultNotEqualTo(Integer value) {
            addCriterion("first_check_all_result <>", value, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultGreaterThan(Integer value) {
            addCriterion("first_check_all_result >", value, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultGreaterThanOrEqualTo(Integer value) {
            addCriterion("first_check_all_result >=", value, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultLessThan(Integer value) {
            addCriterion("first_check_all_result <", value, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultLessThanOrEqualTo(Integer value) {
            addCriterion("first_check_all_result <=", value, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultIn(List<Integer> values) {
            addCriterion("first_check_all_result in", values, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultNotIn(List<Integer> values) {
            addCriterion("first_check_all_result not in", values, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultBetween(Integer value1, Integer value2) {
            addCriterion("first_check_all_result between", value1, value2, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllResultNotBetween(Integer value1, Integer value2) {
            addCriterion("first_check_all_result not between", value1, value2, "firstCheckAllResult");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeIsNull() {
            addCriterion("first_check_all_time is null");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeIsNotNull() {
            addCriterion("first_check_all_time is not null");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeEqualTo(Date value) {
            addCriterion("first_check_all_time =", value, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeNotEqualTo(Date value) {
            addCriterion("first_check_all_time <>", value, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeGreaterThan(Date value) {
            addCriterion("first_check_all_time >", value, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("first_check_all_time >=", value, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeLessThan(Date value) {
            addCriterion("first_check_all_time <", value, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeLessThanOrEqualTo(Date value) {
            addCriterion("first_check_all_time <=", value, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeIn(List<Date> values) {
            addCriterion("first_check_all_time in", values, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeNotIn(List<Date> values) {
            addCriterion("first_check_all_time not in", values, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeBetween(Date value1, Date value2) {
            addCriterion("first_check_all_time between", value1, value2, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andFirstCheckAllTimeNotBetween(Date value1, Date value2) {
            addCriterion("first_check_all_time not between", value1, value2, "firstCheckAllTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeIsNull() {
            addCriterion("last_operation_time is null");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeIsNotNull() {
            addCriterion("last_operation_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeEqualTo(Date value) {
            addCriterion("last_operation_time =", value, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeNotEqualTo(Date value) {
            addCriterion("last_operation_time <>", value, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeGreaterThan(Date value) {
            addCriterion("last_operation_time >", value, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_operation_time >=", value, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeLessThan(Date value) {
            addCriterion("last_operation_time <", value, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_operation_time <=", value, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeIn(List<Date> values) {
            addCriterion("last_operation_time in", values, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeNotIn(List<Date> values) {
            addCriterion("last_operation_time not in", values, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeBetween(Date value1, Date value2) {
            addCriterion("last_operation_time between", value1, value2, "lastOperationTime");
            return (Criteria) this;
        }

        public Criteria andLastOperationTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_operation_time not between", value1, value2, "lastOperationTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}