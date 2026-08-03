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


class ReportResult(BaseModel):
    dimensionScores: dict
    percentileRanks: dict
    tags: List[str]
    redFlags: List[str]
    validityScore: int


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
    return {"message": "天赋探索算法服务 v1.0"}


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

    logger.info(f"计算完成: scores={dim_scores}, validity={validity}")

    red_flags = []
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
        validityScore=validity
    )