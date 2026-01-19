import { MapPin, DollarSign, Clock, Building2, ExternalLink } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";

interface Job {
  id: string;
  title: string;
  company: string;
  location: string;
  salary: string;
  type: string;
  matchScore: number;
  logo: string;
  postedAgo: string;
  skills: string[];
}

interface JobCardProps {
  job: Job;
  onApply: (jobId: string) => void;
}

const JobCard = ({ job, onApply }: JobCardProps) => {
  const getMatchColor = () => {
    if (job.matchScore >= 85) return "bg-success text-success-foreground";
    if (job.matchScore >= 70) return "bg-primary text-primary-foreground";
    return "bg-warning text-warning-foreground";
  };

  return (
    <Card className="p-6 shadow-card hover:shadow-card-hover transition-all duration-300 hover:-translate-y-1 group">
      <div className="flex items-start gap-4">
        <div className="flex h-14 w-14 items-center justify-center rounded-xl bg-secondary text-2xl font-bold text-primary">
          {job.logo}
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-start justify-between gap-4">
            <div>
              <h3 className="font-display text-lg font-semibold text-foreground group-hover:text-primary transition-colors">
                {job.title}
              </h3>
              <div className="flex items-center gap-2 mt-1 text-muted-foreground">
                <Building2 className="h-4 w-4" />
                <span className="text-sm">{job.company}</span>
              </div>
            </div>
            <Badge className={getMatchColor()}>
              {job.matchScore}% Match
            </Badge>
          </div>

          <div className="flex flex-wrap gap-4 mt-4 text-sm text-muted-foreground">
            <div className="flex items-center gap-1">
              <MapPin className="h-4 w-4" />
              {job.location}
            </div>
            <div className="flex items-center gap-1">
              <DollarSign className="h-4 w-4" />
              {job.salary}
            </div>
            <div className="flex items-center gap-1">
              <Clock className="h-4 w-4" />
              {job.postedAgo}
            </div>
          </div>

          <div className="flex flex-wrap gap-2 mt-4">
            {job.skills.slice(0, 4).map((skill, index) => (
              <Badge key={index} variant="secondary" className="text-xs">
                {skill}
              </Badge>
            ))}
            {job.skills.length > 4 && (
              <Badge variant="secondary" className="text-xs">
                +{job.skills.length - 4} more
              </Badge>
            )}
          </div>

          <div className="flex items-center justify-between mt-6 pt-4 border-t border-border">
            <span className="text-sm text-muted-foreground">{job.type}</span>
            <div className="flex gap-2">
              <Button variant="outline" size="sm">
                <ExternalLink className="h-4 w-4 mr-1" />
                Details
              </Button>
              <Button 
                variant="gradient" 
                size="sm"
                onClick={() => onApply(job.id)}
              >
                Quick Apply
              </Button>
            </div>
          </div>
        </div>
      </div>
    </Card>
  );
};

export default JobCard;
