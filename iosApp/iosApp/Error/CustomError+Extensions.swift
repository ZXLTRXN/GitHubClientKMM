//
//  CustomErrorExtension.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 24.06.2022.
//


import shared

extension CustomError : LocalizedError {
    public var errorDescription: String? {
        switch self {
        case is Connection:
            return NSLocalizedString("errorType.connectionError.description", comment: "")
        case is Unauthorized:
            return NSLocalizedString("errorType.wrongToken.description", comment: "")
        case is Forbidden:
            return NSLocalizedString("errorType.noRights.description", comment: "")
        case is NotFound:
            return NSLocalizedString("errorType.readmeNotFound.description", comment: "")
        case is UnknownRequest:
            return NSLocalizedString("errorType.unknownError.description", comment: "")
        case is Unknown:
            return NSLocalizedString("errorType.unknownError.description", comment: "")
        default:
            return nil
        }
    }
    
    public var failureReason: String? {
        guard self is UnknownRequest || self is Unknown else { return nil }
        
        if self is Unknown {
            return String(
                format: NSLocalizedString("errorType.unknownError.failureReason", comment: ""),
                (self as! Unknown).message
            )
        }
        
        let unknownError = self as! UnknownRequest
        return String(
            format: NSLocalizedString("errorType.unknownError.failureReasonWithCode", comment: ""),
            unknownError.body,
            Int(unknownError.code)
        )
    }
    
    public var recoverySuggestion: String? {
        switch self {
        case is Connection:
            return NSLocalizedString("errorType.connectionError.suggestion", comment: "")
        case is Unauthorized:
            return NSLocalizedString("errorType.wrongToken.suggestion", comment: "")
        case is Forbidden:
            return NSLocalizedString("errorType.noRights.suggestion", comment: "")
        case is NotFound:
            return NSLocalizedString("errorType.readmeNotFound.suggestion", comment: "")
        case is UnknownRequest:
            return NSLocalizedString("errorType.unknownError.suggestion", comment: "")
        case is Unknown:
            return NSLocalizedString("errorType.unknownError.suggestion", comment: "")
        default:
            return nil
        }
    }
}
