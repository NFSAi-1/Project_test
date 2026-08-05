from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List, Optional
import numpy as np
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(title="天赋探索算法服务")


# ==================== 数据模型 ====================
class AnswerItem(BaseModel):
    questionId: int
    score: int
    responseTime: Optional[int] = None


class AnswerRequest(BaseModel):
    userId: str
    answers: List[AnswerItem]


class SubjectDirection(BaseModel):
    direction: str
    score: int
    subjects: List[str]


class EducationResult(BaseModel):
    predictedDegree: str
    degreeConfidence: int
    subjectDirections: List[SubjectDirection]
    educationAnalysis: str


class DimensionItem(BaseModel):
    name: str
    score: int
    percentile: int
    level: str


class StrengthItem(BaseModel):
    name: str
    description: str


class StrengthResult(BaseModel):
    dimensions: List[DimensionItem]
    strengths: List[StrengthItem]
    weaknesses: List[StrengthItem]
    overallSummary: str


class CareerPath(BaseModel):
    career: str
    matchScore: int
    reason: str


class PlanPhase(BaseModel):
    phase: str
    actions: List[str]
    timeline: str


class PlanningResult(BaseModel):
    careerPaths: List[CareerPath]
    shortTermPlan: List[PlanPhase]
    longTermPlan: List[PlanPhase]
    planningSummary: str


class ReportResult(BaseModel):
    dimensionScores: dict
    percentileRanks: dict
    tags: List[str]
    redFlags: List[str]
    validityScore: int
    taskId: Optional[str] = None
    education: Optional[EducationResult] = None
    strengths: Optional[StrengthResult] = None
    planning: Optional[PlanningResult] = None


# ==================== 核心算法 ====================
def compute_dimension_scores(answers: List[AnswerItem]) -> dict:
    score_map = {
        1: 'openness', 2: 'openness', 3: 'openness', 4: 'openness', 5: 'openness',
        6: 'conscientiousness', 7: 'conscientiousness', 8: 'conscientiousness',
        9: 'conscientiousness', 10: 'conscientiousness',
        11: 'extraversion', 12: 'extraversion', 13: 'extraversion',
        14: 'extraversion', 15: 'extraversion',
        16: 'stability', 17: 'stability', 18: 'stability',
        19: 'stability', 20: 'stability'
    }
    dim_scores = {'openness': [], 'conscientiousness': [],
                  'extraversion': [], 'stability': []}
    for item in answers:
        dim = score_map.get(item.questionId)
        if dim:
            dim_scores[dim].append(item.score)
    result = {}
    for dim, scores in dim_scores.items():
        if scores:
            avg = sum(scores) / len(scores)
            result[dim] = int(avg * 20)
        else:
            result[dim] = 50
    return result


def compute_validity(answers: List[AnswerItem]) -> int:
    lie_scores = []
    for item in answers:
        if 21 <= item.questionId <= 23:
            lie_scores.append(item.score)
    avg = sum(lie_scores) / len(lie_scores) if lie_scores else 3
    return int(avg * 20)


def match_tags(scores: dict) -> List[str]:
    tags = []
    # 开放性
    if scores.get('openness', 0) > 70:
        tags.append("好奇探索家")
    elif scores.get('openness', 0) > 50:
        tags.append("务实思考者")
    else:
        tags.append("传统偏好者")
    # 尽责性
    if scores.get('conscientiousness', 0) > 70:
        tags.append("自律规划师")
    elif scores.get('conscientiousness', 0) > 50:
        tags.append("灵活执行者")
    else:
        tags.append("随性创作者")
    # 外向性
    if scores.get('extraversion', 0) > 70:
        tags.append("社交充电型")
    elif scores.get('extraversion', 0) > 50:
        tags.append("平衡社交型")
    else:
        tags.append("独处蓄能型")
    # 稳定性
    if scores.get('stability', 0) > 70:
        tags.append("情绪磐石")
    elif scores.get('stability', 0) > 50:
        tags.append("情绪稳健")
    else:
        tags.append("高敏感体质")
    return tags


def compute_percentiles(scores: dict, norm_data: dict) -> dict:
    percentiles = {}
    for dim, score in scores.items():
        data = norm_data.get(dim, [50, 60, 70, 80, 90])
        pct = np.mean(np.array(data) < score) * 100
        percentiles[dim] = int(round(pct))
    return percentiles


# 常模数据
NORM_DATA = {
    'openness': np.random.normal(65, 15, 1000).clip(0, 100),
    'conscientiousness': np.random.normal(60, 15, 1000).clip(0, 100),
    'extraversion': np.random.normal(55, 15, 1000).clip(0, 100),
    'stability': np.random.normal(50, 15, 1000).clip(0, 100)
}


# ==================== 接口 ====================
@app.get("/")
def root():
    return {"message": "天赋探索算法服务 v2.0"}


def analyze_education(scores: dict) -> EducationResult:
    """学历发展趋势预测"""
    openness = scores.get("openness", 50)
    conscientiousness = scores.get("conscientiousness", 50)
    avg_score = (openness + conscientiousness) / 2

    if avg_score >= 80:
        predicted_degree = "博士"
        confidence = min(90, int(avg_score))
    elif avg_score >= 65:
        predicted_degree = "硕士"
        confidence = min(85, int(avg_score))
    elif avg_score >= 50:
        predicted_degree = "本科"
        confidence = min(80, int(avg_score))
    else:
        predicted_degree = "专科/职业培训"
        confidence = min(75, int(avg_score + 10))

    directions = []
    if openness >= 60:
        directions.append(SubjectDirection(
            direction="理工科",
            score=min(95, openness + 10),
            subjects=["计算机科学", "数据科学", "人工智能", "数学"]
        ))
    if openness >= 50:
        directions.append(SubjectDirection(
            direction="人文社科",
            score=min(90, openness + 5),
            subjects=["心理学", "社会学", "教育学", "哲学"]
        ))
    if conscientiousness >= 55:
        directions.append(SubjectDirection(
            direction="商科",
            score=min(90, conscientiousness + 5),
            subjects=["金融学", "管理学", "会计学", "市场营销"]
        ))
    if openness >= 70:
        directions.append(SubjectDirection(
            direction="艺术设计",
            score=min(90, openness),
            subjects=["视觉传达", "工业设计", "数字媒体", "建筑学"]
        ))

    analysis = (
        f"基于您的开放性得分({openness}分)和尽责性得分({conscientiousness}分)综合评估，"
        f"预测您具备攻读{predicted_degree}学位的潜力（置信度{confidence}%）。"
        f"{'您在开放性维度表现突出，适合需要创造力和抽象思维的学科方向。' if openness >= 70 else ''}"
        f"{'您在尽责性维度表现良好，具备优秀的自我管理和长期规划能力，这对学术深造非常重要。' if conscientiousness >= 65 else ''}"
    )

    return EducationResult(
        predictedDegree=predicted_degree,
        degreeConfidence=confidence,
        subjectDirections=directions,
        educationAnalysis=analysis
    )


def analyze_strengths(scores: dict) -> StrengthResult:
    """个人优势与不足评估"""
    openness = scores.get("openness", 50)
    conscientiousness = scores.get("conscientiousness", 50)
    extraversion = scores.get("extraversion", 50)
    stability = scores.get("stability", 50)

    def level(score):
        if score >= 80: return "优秀"
        if score >= 65: return "良好"
        if score >= 45: return "一般"
        return "待提升"

    creativity_score = min(100, int(openness * 1.1))
    logic_score = min(100, int((openness * 0.6 + conscientiousness * 0.4) * 1.05))
    execution_score = min(100, int((conscientiousness * 0.8 + stability * 0.2)))
    communication_score = min(100, int(extraversion * 1.05))
    leadership_score = min(100, int((extraversion * 0.5 + conscientiousness * 0.3 + stability * 0.2)))
    emotional_score = min(100, int(stability * 1.05))

    dimensions = [
        DimensionItem(name="逻辑思维", score=logic_score, percentile=min(99, logic_score), level=level(logic_score)),
        DimensionItem(name="创造力", score=creativity_score, percentile=min(99, creativity_score), level=level(creativity_score)),
        DimensionItem(name="执行力", score=execution_score, percentile=min(99, execution_score), level=level(execution_score)),
        DimensionItem(name="沟通能力", score=communication_score, percentile=min(99, communication_score), level=level(communication_score)),
        DimensionItem(name="领导力", score=leadership_score, percentile=min(99, leadership_score), level=level(leadership_score)),
        DimensionItem(name="情绪管理", score=emotional_score, percentile=min(99, emotional_score), level=level(emotional_score)),
    ]

    strengths = []
    weaknesses = []
    for d in dimensions:
        if d.score >= 65:
            strengths.append(StrengthItem(
                name=d.name,
                description=f"得分{d.score}分，处于{d.level}水平，该能力是您的核心竞争力之一。"
            ))
        elif d.score < 50:
            weaknesses.append(StrengthItem(
                name=d.name,
                description=f"得分{d.score}分，该领域有较大的提升空间，建议通过针对性训练加强。"
            ))

    top_strength = max(dimensions, key=lambda d: d.score)
    bottom_weakness = min(dimensions, key=lambda d: d.score)
    summary = (
        f"综合分析，您的核心优势集中在{top_strength.name}方面（{top_strength.score}分，{top_strength.level}），"
        f"而在{bottom_weakness.name}方面（{bottom_weakness.score}分，{bottom_weakness.level}）有提升空间。"
        f"{'建议多参与团队协作和社交活动来提升沟通能力。' if communication_score < 50 else ''}"
        f"{'建议通过制定详细计划和设定明确目标来加强执行力。' if execution_score < 50 else ''}"
    )

    return StrengthResult(
        dimensions=dimensions,
        strengths=strengths,
        weaknesses=weaknesses,
        overallSummary=summary
    )


def analyze_planning(scores: dict, education: EducationResult) -> PlanningResult:
    """职业发展与学业规划建议"""
    openness = scores.get("openness", 50)
    conscientiousness = scores.get("conscientiousness", 50)
    extraversion = scores.get("extraversion", 50)
    stability = scores.get("stability", 50)

    career_paths = []
    if openness >= 60 and conscientiousness >= 55:
        career_paths.append(CareerPath(
            career="数据科学家",
            matchScore=min(92, (openness + conscientiousness) // 2 + 5),
            reason="逻辑思维与创造力突出，适合数据驱动型分析和建模工作。"
        ))
    if extraversion >= 55 and conscientiousness >= 50:
        career_paths.append(CareerPath(
            career="产品经理",
            matchScore=min(90, (extraversion + conscientiousness) // 2 + 3),
            reason="沟通能力与执行力兼备，能够有效协调团队并推动产品落地。"
        ))
    if openness >= 65:
        career_paths.append(CareerPath(
            career="软件架构师",
            matchScore=min(88, openness + 5),
            reason="开放性和创造力突出，适合复杂系统设计和创新性技术工作。"
        ))
    if stability >= 60 and conscientiousness >= 55:
        career_paths.append(CareerPath(
            career="金融分析师",
            matchScore=min(88, (stability + conscientiousness) // 2 + 5),
            reason="情绪稳定且做事严谨，适合高风险环境下的金融决策分析。"
        ))
    if extraversion >= 65:
        career_paths.append(CareerPath(
            career="管理咨询顾问",
            matchScore=min(87, extraversion + 5),
            reason="出色的社交和沟通能力，适合客户导向型的咨询服务工作。"
        ))

    degree = education.predictedDegree
    short_term = []
    if "专科" in degree or "本科" in degree:
        short_term.append(PlanPhase(
            phase="大学阶段",
            actions=["夯实专业基础知识", "参加学科竞赛和项目实践", "争取实习和科研机会", "考取相关职业资格证书"],
            timeline="1-4年"
        ))
    if "硕士" in degree or "博士" in degree:
        short_term.append(PlanPhase(
            phase="研究生阶段",
            actions=["深入专业领域研究", "发表高质量学术论文", "建立行业人脉网络", "参与导师科研项目"],
            timeline="2-6年"
        ))

    long_term = [
        PlanPhase(
            phase="职场初期",
            actions=["积累行业经验，建立专业口碑", "持续学习新技术和新方法", "明确职业发展方向"],
            timeline="1-3年"
        ),
        PlanPhase(
            phase="职业成长期",
            actions=["承担更大的项目责任", "培养团队管理和领导能力", "建立个人品牌和行业影响力"],
            timeline="3-8年"
        ),
        PlanPhase(
            phase="职业成熟期",
            actions=["成为领域专家或管理者", "指导培养新人", "探索跨领域发展机会"],
            timeline="8年以上"
        ),
    ]

    summary = (
        f"根据您的性格特质分析，建议优先考虑{career_paths[0].career if career_paths else '技术型'}方向。"
        f"短期应聚焦于{'学术深造和专业技能积累' if '硕士' in degree or '博士' in degree else '专业学习和实践能力培养'}，"
        f"长期朝着{'成为行业专家' if openness >= 65 else '成为复合型人才'}的目标发展。"
    )

    return PlanningResult(
        careerPaths=career_paths,
        shortTermPlan=short_term,
        longTermPlan=long_term,
        planningSummary=summary
    )


@app.post("/compute", response_model=ReportResult)
async def compute_report(request: AnswerRequest):
    logger.info(f"收到计算请求: userId={request.userId}, answers={len(request.answers)}")

    if not request.answers or len(request.answers) < 25:
        logger.warning(f"答案数量不足: {len(request.answers) if request.answers else 0}")
        raise HTTPException(status_code=400, detail="需要完整的25题答案")

    dim_scores = compute_dimension_scores(request.answers)
    percentiles = compute_percentiles(dim_scores, NORM_DATA)
    tags = match_tags(dim_scores)
    validity = compute_validity(request.answers)

    education = analyze_education(dim_scores)
    strengths = analyze_strengths(dim_scores)

    all_ones = all(item.score == 1 for item in request.answers)
    if all_ones:
        planning = None
        logger.info(f"所有答案均为1，跳过发展规划生成")
    else:
        planning = analyze_planning(dim_scores, education)

    logger.info(f"计算完成: scores={dim_scores}, validity={validity}, degree={education.predictedDegree}")

    red_flags = []
    if all_ones:
        red_flags.append("您对所有问题的回答均为'非常不符合'，测试结果参考价值有限，建议认真阅读题目后重新测试")
    if dim_scores.get('stability', 50) < 30:
        red_flags.append("情绪敏感性较高，建议学习压力管理技巧")
    if dim_scores.get('conscientiousness', 50) < 25:
        red_flags.append("计划执行能力较弱，可尝试番茄工作法")
    if validity < 40:
        red_flags.append("测试结果参考价值有限，建议静心后重测")

    return ReportResult(
        dimensionScores=dim_scores,
        percentileRanks=percentiles,
        tags=tags,
        redFlags=red_flags,
        validityScore=validity,
        education=education,
        strengths=strengths,
        planning=planning
    )